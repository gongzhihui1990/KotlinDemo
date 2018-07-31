/*
 * Copyright (C) 2016 Francisco José Montiel Navarro.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bus.data.http.persistentcookiejar;



import com.bus.data.http.persistentcookiejar.cache.CookieCache;
import com.bus.data.http.persistentcookiejar.persistence.CookiePersistor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import framework.util.Loger;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * @author caroline
 */
public class PersistentCookieJar implements ClearAbleCookieJar {

    private CookieCache cache;
    private CookiePersistor persistor;

    public PersistentCookieJar(CookieCache cache, CookiePersistor persistor) {
        this.cache = cache;
        this.persistor = persistor;
        this.cache.addAll(persistor.loadAll());
    }

    private static List<Cookie> filterPersistentCookies(List<Cookie> cookies) {
        List<Cookie> persistentCookies = new ArrayList<>();
        for (Cookie cookie : cookies) {
            Loger.INSTANCE.e("cookie "+cookie.toString());
            //if (cookie.persistent()) {
                persistentCookies.add(cookie);
            //}
        }
        return persistentCookies;
    }

    private static boolean isCookieExpired(Cookie cookie) {
        return cookie.expiresAt() < System.currentTimeMillis();
    }

    @Override
    synchronized public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        Loger.INSTANCE.i("cookies size "+cookies.size());
        cache.addAll(cookies);
        List<Cookie> persistentCookies = filterPersistentCookies(cookies);
        if (!persistentCookies.isEmpty()) {
            Loger.INSTANCE.e("save");
            persistor.saveAll(persistentCookies);
        }else{
            Loger.INSTANCE.w("not save");
        }
    }

    @Override
    synchronized public List<Cookie> loadForRequest(HttpUrl url) {
        Loger.INSTANCE.i("log");
        List<Cookie> cookiesToRemove = new ArrayList<>();
        List<Cookie> validCookies = new ArrayList<>();

        for (Iterator<Cookie> it = cache.iterator(); it.hasNext(); ) {
            Cookie currentCookie = it.next();

            if (isCookieExpired(currentCookie)) {
                cookiesToRemove.add(currentCookie);
                it.remove();
            } else if (currentCookie.matches(url)) {
                validCookies.add(currentCookie);
            }
        }
        if (!cookiesToRemove.isEmpty()) {
            persistor.removeAll(cookiesToRemove);
        }
        Loger.INSTANCE.i("log cookies size "+validCookies.size());
        return validCookies;
    }

    @Override
    synchronized public void clearSession() {
        Loger.INSTANCE.i("log");
        cache.clear();
        cache.addAll(persistor.loadAll());
    }

    @Override
    synchronized public void clear() {
        Loger.INSTANCE.i("log");
        cache.clear();
        persistor.clear();
    }
}
