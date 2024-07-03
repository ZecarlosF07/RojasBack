package Rojas.project.Configure.Repository;

import Rojas.project.Application.Repository.IRepository;

import java.util.List;

public class UserRepository implements IRepository<UserRepository> {
    @Override
    public UserRepository Create(UserRepository entity){
        return null;
    }

    @Override
    public void Delete(String id) {

    }

    @Override
    public UserRepository Update(UserRepository entity) {
        return null;
    }

    @Override
    public UserRepository Get(String id) {
        return null;
    }

    @Override
    public List<UserRepository> GetAll() {
        return null;
    }
}
