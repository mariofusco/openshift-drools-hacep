/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.hacep.util;

import org.kie.hacep.EnvConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrinterUtil {

    private static final Logger logger = LoggerFactory.getLogger(PrinterUtil.class);

    public static Printer getPrinter( EnvConfig config){
        if(config.getPrinterType().equals(PrinterLogImpl.class.getName())){
            return new PrinterLogImpl();
        }else{
            Printer returnInstance;
            try {
                returnInstance = (Printer) Class.forName(config.getPrinterType()).newInstance();
            }catch (Exception ex){
                logger.error("Printer:{} not found, using PrinterLog", ex.getMessage());
                return new PrinterLogImpl();
            }
            return returnInstance;
        }
    }

    public static Logger getKafkaLoggerForTest(EnvConfig config){
        if(config.isUnderTest() && !config.getPrinterType().equals(PrinterLogImpl.class.getName())){
            return LoggerFactory.getLogger("org.hacep");
        }else {
            return null;
        }
    }
}
