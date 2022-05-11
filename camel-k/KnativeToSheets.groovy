// camel-k: config=secret:google-secret trait=knative-service.min-scale=0
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Arrays;
import java.util.Map;

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import com.google.api.services.sheets.v4.model.ValueRange;

from("knative:channel/feedback")
        .log("${body}")
        .unmarshal().json(JsonLibrary.Jackson, java.util.Map.class)
        .log("unmarshalled: ${body}")
        .process(exchange -> {
            final Message m = exchange.getMessage();
            Map<String, Object> body = m.getBody(Map.class);
            m.setHeader("CamelGoogleSheets.valueInputOption", "RAW");
            m.setHeader("CamelGoogleSheets.values",
                    new ValueRange().setValues(
                            Arrays.asList(
                                    Arrays.asList(
                                            body.get("dateTime"),
                                            body.get("userName"),
                                            body.get("text")
                                            ))));

        })
        // Throttle to avoid exceeding the default Google API limit of 60 requests per min
        .throttle(1).timePeriodMillis(1500)
        .to("google-sheets://data/append?spreadsheetId={{google-sheets.spreadsheet-id}}&range=Sheet1!A1:A3");
