/*
 * Copyright 2015 MongoDB, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package course.homework;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.bson.Document;
import org.bson.types.ObjectId;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.StringWriter;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.*;

public class MongoDBSparkFreemarkerStyle {
    public static void main(String[] args) {
        final Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(MongoDBSparkFreemarkerStyle.class, "/freemarker");


        Spark.get(new Route("/homework2-3") {
            @Override
            public Object handle(final Request request,
                                 final Response response) {
                StringWriter writer = new StringWriter();

                try (MongoClient client = new MongoClient()) {

                    MongoDatabase database = client.getDatabase("students");
                    final MongoCollection<Document> collection = database.getCollection("grades");

                    Template template = configuration.getTemplate("answer.ftl");

                    List<Document> results =
                            collection.find(eq("type", "homework"))
                                    .sort(orderBy(ascending("student_id"), descending("score")))
                                    .into(new ArrayList<>());

                    int currentStudentId = -1;
                    ObjectId currentObjectId = null;
                    for (Document cur : results) {
                        int localStudentId = (Integer) cur.get("student_id");

                        if (currentObjectId == null) currentObjectId = (ObjectId) cur.get("_id");

                        if (currentStudentId == -1) currentStudentId = localStudentId;

                        if (localStudentId != currentStudentId) {
                            currentStudentId = localStudentId;
                            collection.deleteOne(eq("_id", currentObjectId));
                        }

                        currentObjectId = (ObjectId) cur.get("_id");
                    }

                    Map<String, String> answerMap = new HashMap<>();
                    answerMap.put("answer", "Finished");

                    template.process(answerMap, writer);
                } catch (Exception e) {
                    e.printStackTrace();
                    halt(500);
                }
                return writer;
            }
        });

        Spark.get(new Route("/homework3-1") {
            @Override
            public Object handle(final Request request,
                                 final Response response) {
                StringWriter writer = new StringWriter();

                try (MongoClient client = new MongoClient()) {

                    MongoDatabase database = client.getDatabase("school");
                    final MongoCollection<Document> collection = database.getCollection("students");

                    Template template = configuration.getTemplate("answer.ftl");

                    final Comparator<Document> documentComparator = (d1, d2) -> {
                        if ("homework".equals(d1.getString("type"))) {
                            if ("homework".equals(d2.getString("type"))) {
                                return (d1.getDouble("score") > d2.getDouble("score")) ? 1 : -1;
                            } else {
                                return -1;
                            }
                        } else if ("homework".equals(d2.getString("type"))) {
                            return +1;
                        } else {
                            return 0;
                        }
                    };

                    Block<Document> deleteLowestHomework = student -> {
                        List<Document> scores = (List) student.get("scores");
                        scores.sort(documentComparator);
                        scores.remove(0);

                        // update student with pruned scores
                        collection.updateOne(eq("_id", student.getInteger("_id")),
                                new Document("$set", new Document("scores", scores)));
                    };

                    collection.find().forEach(deleteLowestHomework);

                    Map<String, String> answerMap = new HashMap<>();
                    answerMap.put("answer", "Finished");

                    template.process(answerMap, writer);
                } catch (Exception e) {
                    e.printStackTrace();
                    halt(500);
                }
                return writer;
            }
        });


    }
}
