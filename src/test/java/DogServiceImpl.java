import annotacion.Inject;

import java.util.List;

public class DogServiceImpl implements DogService {
    private DogDaoImpl dogDao;

    @Inject
    public DogServiceImpl(DogDaoImpl dogDao) {
        this.dogDao = dogDao;
    }



    @Override
    public List<Dog> get() {
        return dogDao.getAll();
    }
}
