/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.springcloud.gray.dubbo.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * In-Memory {@link UserService} implementation.
 */
@DubboService(protocol = "dubbo")
public class InMemoryUserService implements UserService {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserService.class);

    private Map<Long, User> usersRepository = new HashMap<>();

    @Override
    public boolean save(User user) {
        return usersRepository.put(user.getId(), user) == null;
    }

    @Override
    public boolean remove(Long userId) {
        return usersRepository.remove(userId) != null;
    }

    @Override
    public Collection<User> findAll() {
        log.info("[web] invoke findAll");
        return usersRepository.values();
    }

}
