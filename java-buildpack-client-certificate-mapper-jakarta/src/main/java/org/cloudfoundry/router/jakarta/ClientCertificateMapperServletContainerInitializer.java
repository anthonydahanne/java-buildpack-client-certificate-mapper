/*
 * Copyright 2017-2023 the original author or authors.
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

package org.cloudfoundry.router.jakarta;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

import java.security.cert.CertificateException;
import java.util.EnumSet;
import java.util.Set;

public final class ClientCertificateMapperServletContainerInitializer implements ServletContainerInitializer {

    public ClientCertificateMapperServletContainerInitializer() {
        System.out.println("Jakarta ClientCertificateMapperServletContainerInitializer");
    }

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        if (ctx == null) {
            return;
        }

        FilterRegistration.Dynamic filterRegistration = addFilter(ctx);
        if (filterRegistration == null) {
            return;
        }

        filterRegistration.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
    }

    private FilterRegistration.Dynamic addFilter(ServletContext ctx) throws ServletException {
        try {
            return ctx.addFilter("clientCertificateMapper", new ClientCertificateMapper());
        } catch (CertificateException e) {
            throw new ServletException(e);
        }
    }

}
