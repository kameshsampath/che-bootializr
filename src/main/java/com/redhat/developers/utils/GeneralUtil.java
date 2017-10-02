/**
 * Copyright (c) 2017 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.redhat.developers.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class GeneralUtil {
    static final Pattern HTTP_GITURL_PATTERN = Pattern.compile("(^(https|http)://.*)(\\.git$)");

    public static String sanitizeGitUrl(String gitUrl) {
        Matcher matcher = HTTP_GITURL_PATTERN.matcher(gitUrl);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return gitUrl;
    }


    /**
     * @param httpUrl
     * @return
     */
    public static String sanitizeUrl(String httpUrl) {

        if (httpUrl.endsWith(".git")) {
            return StringUtils.replace(httpUrl, ".git", "");
        }
        return httpUrl;
    }

    /**
     * @param projectArtifactId
     * @param extension
     * @return
     */
    public static String sanitizedUrlEncodedName(String projectArtifactId, String extension) {
        String baseName = StringUtils.replace(projectArtifactId, " ", "_");
        try {
            return URLEncoder.encode(baseName, "UTF-8") + "." + extension;
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }


    public static String marshallToJson(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Error while marshalling", e);
        }
        return "{}";
    }
}