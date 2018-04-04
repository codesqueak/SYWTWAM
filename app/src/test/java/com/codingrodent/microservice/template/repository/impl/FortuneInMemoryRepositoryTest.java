/*
 * MIT License
 *
 * Copyright (c) 2017
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package com.codingrodent.microservice.template.repository.impl;

import com.codingrodent.microservice.template.entity.FortuneEntity;
import org.junit.*;
import org.springframework.data.domain.*;

import java.util.*;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FortuneInMemoryRepositoryTest {
    private FortuneInMemoryRepository repository;
    private Set<String> uuids;
    private int authors;

    @Before
    public void setUp() {
        repository = new FortuneInMemoryRepository();
        uuids = new HashSet<>();
        authors = 0;
        String author, uuid;
        for (int i = 0; i < 20; i++) {
            uuid = UUID.randomUUID().toString();
            if (0 == i % 3)
                author = "";
            else {
                authors++;
                author = "author" + i;
            }
            repository.save(new FortuneEntity(uuid, "text" + i, author));
            uuids.add(uuid);
        }
    }


    @Test
    public void findAllNamed() {
        List<FortuneEntity> list = repository.findAllNamed(new PageRequest(0, 999));
        assertEquals(authors, list.size());
        for (FortuneEntity entity : list)
            assertTrue(uuids.contains(entity.getId()));
        //
        list = repository.findAllNamed(new PageRequest(0, 1));
        assertEquals(1, list.size());
        //
        list = repository.findAllNamed(new PageRequest(0, 3));
        assertEquals(3, list.size());
        //
        list = repository.findAllNamed(new PageRequest(0, 4));
        assertEquals(4, list.size());
        //
        list = repository.findAllNamed(new PageRequest(1, 1));
        assertEquals(1, list.size());
        //
        list = repository.findAllNamed(new PageRequest(1, 4));
        assertEquals(4, list.size());
        //
        list = repository.findAllNamed(new PageRequest(999, 3));
        assertEquals(0, list.size());
    }

    @Test
    public void findAllAnon() {
        List<FortuneEntity> list = repository.findAllAnon(new PageRequest(0, 999));
        assertEquals(uuids.size() - authors, list.size());
        for (FortuneEntity entity : list)
            assertTrue(uuids.contains(entity.getId()));
        //
        list = repository.findAllAnon(new PageRequest(0, 1));
        assertEquals(1, list.size());
        //
        list = repository.findAllAnon(new PageRequest(0, 3));
        assertEquals(3, list.size());
        //
        list = repository.findAllAnon(new PageRequest(0, 4));
        assertEquals(4, list.size());
        //
        list = repository.findAllAnon(new PageRequest(1, 1));
        assertEquals(1, list.size());
        //
        list = repository.findAllAnon(new PageRequest(1, 4));
        assertEquals(3, list.size());
        //
        list = repository.findAllAnon(new PageRequest(999, 3));
        assertEquals(0, list.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void findAllPageable1() {
        repository.findAll((Pageable) null).getContent();
    }

    @Test
    public void findAllPageable2() {
        List<FortuneEntity> list = repository.findAll(new PageRequest(0, 999)).getContent();
        assertEquals(uuids.size(), list.size());
        for (FortuneEntity entity : list)
            assertTrue(uuids.contains(entity.getId()));
        //
        list = repository.findAll(new PageRequest(0, 1)).getContent();
        assertEquals(1, list.size());
        //
        list = repository.findAll(new PageRequest(0, 3)).getContent();
        assertEquals(3, list.size());
        //
        list = repository.findAll(new PageRequest(0, 4)).getContent();
        assertEquals(4, list.size());
        //
        list = repository.findAll(new PageRequest(1, 1)).getContent();
        assertEquals(1, list.size());
        //
        list = repository.findAll(new PageRequest(1, 4)).getContent();
        assertEquals(4, list.size());
        //
        list = repository.findAll(new PageRequest(999, 3)).getContent();
        assertEquals(0, list.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void save1() {
        repository.save((FortuneEntity) null);
    }

    @Test
    public void save2() {
        String uuid = UUID.randomUUID().toString();
        FortuneEntity entity = repository.save(new FortuneEntity(uuid, "text", "author"));
        assertEquals("author", entity.getAuthor());
        assertEquals("text", entity.getText());
        assertEquals(0, entity.getVersion());
        assertEquals(uuid, entity.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveIterable1() {
        repository.save((Iterable<FortuneEntity>) null);
    }

    @Test
    public void saveIterable2() {
        final int size = 7;
        Set<String> uuids = new HashSet<>();
        List<FortuneEntity> entities = new LinkedList<>();
        // create
        for (int i = 0; i < size; i++) {
            String uuid = UUID.randomUUID().toString();
            uuids.add(uuid);
            entities.add(repository.save(new FortuneEntity(uuid, "text" + i, "author" + i)));
        }
        // save
        Iterable<FortuneEntity> iterable = repository.save(entities);
        // check
        int count = 0;
        for (FortuneEntity entity : iterable) {
            assertTrue(uuids.contains(entity.getId()));
            count++;
        }
        assertEquals(size, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findOne1() {
        repository.findOne(null);
    }

    @Test
    public void findOne2() {
        for (String uuid : uuids)
            assertNotNull(repository.findOne(uuid));
        assertNull(repository.findOne(UUID.randomUUID().toString()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void exists1() {
        assertTrue(repository.exists(null));
    }

    @Test
    public void exists2() {
        for (String uuid : uuids)
            assertTrue(repository.exists(uuid));
        assertFalse(repository.exists(UUID.randomUUID().toString()));
    }

    @Test
    public void findAll() {
        Iterable<FortuneEntity> iterable = repository.findAll();
        // check
        int count = 0;
        for (FortuneEntity entity : iterable) {
            assertTrue(uuids.contains(entity.getId()));
            count++;
        }
        assertEquals(uuids.size(), count);
    }

    @Test
    public void count() {
        assertEquals(uuids.size(), repository.count());
        repository.save(new FortuneEntity(UUID.randomUUID().toString(), "text", "author"));
        assertEquals(uuids.size() + 1, repository.count());
    }

    @Test(expected = IllegalArgumentException.class)
    public void delete1() {
        repository.delete((String) null);
    }

    @Test
    public void delete2() {
        // make sure nothing happens with random uuid
        assertEquals(uuids.size(), repository.count());
        repository.delete(UUID.randomUUID().toString());
        assertEquals(uuids.size(), repository.count());
        int count = uuids.size();
        for (String uuid : uuids) {
            repository.delete(uuid);
            assertEquals(--count, repository.count());
        }
    }

    @Test
    public void deleteAll() {
        assertEquals(uuids.size(), repository.count());
        repository.deleteAll();
        assertEquals(0, repository.count());
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteEntity1() {
        repository.delete((FortuneEntity) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteEntity2() {
        repository.delete(new FortuneEntity(null, "text", "author"));
    }

    @Test
    public void deleteEntity3() {
        assertEquals(uuids.size(), repository.count());
        String uuid = uuids.toArray(new String[0])[0];
        repository.delete(new FortuneEntity(uuid, "text", "author"));
        assertEquals(uuids.size() - 1, repository.count());
    }

    @Test(expected = IllegalArgumentException.class)
    public void findAllIterable1() {
        repository.findAll((Iterable<String>) null);
    }

    @Test
    public void findAllIterable2() {
        Set<String> toFind = new HashSet<>();
        String[] allUuids = uuids.toArray(new String[0]);
        toFind.add(allUuids[0]);
        toFind.add(allUuids[1]);
        toFind.add(UUID.randomUUID().toString());
        toFind.add(allUuids[uuids.size() - 1]);
        Iterable<FortuneEntity> iter = repository.findAll(toFind);
        iter.forEach(e -> assertTrue(toFind.contains(e.getId())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteIterable1() {
        repository.delete((Iterable<FortuneEntity>) null);
    }

    @Test
    public void deleteIterable2() {
        LinkedList<String> toDelete = new LinkedList<>();
        String[] allUuids = uuids.toArray(new String[0]);
        toDelete.add(allUuids[0]);
        toDelete.add(allUuids[1]);
        toDelete.add(UUID.randomUUID().toString());
        toDelete.add(allUuids[uuids.size() - 1]);
        repository.delete(repository.findAll(toDelete));
        assertEquals(uuids.size() - 3, repository.count());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void findAllSort1() {
        repository.findAll(new Sort(new Sort.Order("xyzzy")));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void findAllSort2() {
        repository.findAll(new Sort(new Sort.Order("xyzzy")));
    }

}