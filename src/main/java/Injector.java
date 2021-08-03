import java.lang.reflect.InvocationTargetException;

public interface Injector {


    <T> Provider<T> getProvider(Class<T> type) throws NoSuchMethodException;

    <T> void bind(Class<T> intf, Class<? extends T> impl) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;


    <T> void bindSingleton(Class<T> intf, Class<? extends T> impl) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}
