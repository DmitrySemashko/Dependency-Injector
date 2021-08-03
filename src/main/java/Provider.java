@FunctionalInterface
public interface Provider<T> {
    T getInstance();
}
