import java.util.ArrayList;
import java.util.List;

public class DogDaoImpl implements DogDao {
    private List<Dog> dogs = new ArrayList<>();
    @Override
    public List<Dog> getAll() {
        dogs.add(new Dog("Fred"));
        return dogs;
    }
}
