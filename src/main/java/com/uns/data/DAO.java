
package com.uns.data;

import java.util.List;

public interface DAO<Entity> {
    public Entity getById(Long id);
    public List<Entity> getAll();
    public Entity update(Entity e);
    public Entity create(Entity e);
    public Entity delete(Entity e);

}
