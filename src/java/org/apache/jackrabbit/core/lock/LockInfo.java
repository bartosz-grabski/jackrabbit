/*
 * Copyright 2004-2005 The Apache Software Foundation or its licensors,
 *                     as applicable.
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
package org.apache.jackrabbit.core.lock;

import org.apache.jackrabbit.core.SessionListener;
import org.apache.jackrabbit.core.SessionImpl;

import javax.jcr.Session;

/**
 * Contains information about a lock and gets placed inside the child
 * information of a {@link PathMap}.
 */
class LockInfo implements SessionListener {

    /**
     * Lock token
     */
    final LockToken lockToken;

    /**
     * Flag indicating whether lock is session scoped
     */
    final boolean sessionScoped;

    /**
     * Flag indicating whether lock is deep
     */
    final boolean deep;

    /**
     * Lock owner, determined on creation time
     */
    final String lockOwner;

    /**
     * Session currently holding lock
     */
    private SessionImpl lockHolder;

    /**
     * Flag indicating whether this lock is live
     */
    private boolean live;

    /**
     * Create a new instance of this class.
     * @param lockToken lock token
     * @param sessionScoped whether lock token is session scoped
     * @param deep whether lock is deep
     * @param lockOwner owner of lock
     */
    public LockInfo(LockToken lockToken, boolean sessionScoped,
                    boolean deep, String lockOwner) {
        this.lockToken = lockToken;
        this.sessionScoped = sessionScoped;
        this.deep = deep;
        this.lockOwner = lockOwner;
        this.live = true;
    }

    /**
     * Set the live flag
     * @param live live flag
     */
    public void setLive(boolean live) {
        this.live = live;
    }

    /**
     * Return the UUID of the lock holding node
     * @return uuid
     */
    public String getUUID() {
        return lockToken.uuid;
    }

    /**
     * Return the session currently holding the lock
     * @return session currently holding the lock
     */
    public SessionImpl getLockHolder() {
        return lockHolder;
    }

    /**
     * Set the session currently holding the lock
     * @param lockHolder session currently holding the lock
     */
    public void setLockHolder(SessionImpl lockHolder) {
        this.lockHolder = lockHolder;
    }

    /**
     * Return the lock token as seen by the session passed as parameter. If
     * this session is currently holding the lock, it will get the lock token
     * itself, otherwise a <code>null</code> string
     */
    public String getLockToken(Session session) {
        if (live && session.equals(lockHolder)) {
            return lockToken.toString();
        }
        return null;
    }

    /**
     * Return a flag indicating whether the lock is live
     * @return <code>true</code> if the lock is live; otherwise <code>false</code>
     */
    public boolean isLive() {
        return live;
    }

    //-------------------------------------------------------< SessionListener >

    /**
     * {@inheritDoc}
     */
    public void loggedOut(SessionImpl session) {
        live = false;
    }
}
