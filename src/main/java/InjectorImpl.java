import annotacion.Inject;
import exception.BindingNotFoundException;
import exception.ConstructorNotFoundException;
import exception.TooManyConstructorsException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class InjectorImpl implements Injector {

    private final Map<Class<?>, Object> singletons = new HashMap<>();

    public <T> Provider<T> getProvider(Class<T> type)  {
        synchronized (type) {
            if (type.isAssignableFrom((Class<?>) singletons.get(type)) || singletons.containsKey(type)) {
                return () -> type.cast(singletons.get(type));
            }
        }
        return null;
    }

    public <T> void bind(Class<T> intf, Class<? extends T> impl) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        singletons.put(intf,instance(intf,impl));

    }

    public <T> void bindSingleton(Class<T> intf, Class<? extends T> impl) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        singletons.put(intf, instance(intf, impl));
    }

    private boolean isAnnotation(Constructor<?> constructor) {
        return constructor.isAnnotationPresent(Inject.class);
    }

    private <T> Object instance(Class<T> intf, Class<? extends T> impl) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {

        if (intf.isAssignableFrom(impl)) {
            Constructor<?>[] constructors = impl.getConstructors();

            if (!validCountAnnotation(constructors)) {
                throw new TooManyConstructorsException("");
            }
            for (Constructor<?> constructor : constructors) {
                if (!isAnnotation(constructor)) {
                    if (constructor.getParameterCount() != 0) {
                        throw new ConstructorNotFoundException("");
                    } else {
                        return constructor.newInstance();
                    }
                }
                Class<?>[] params = constructor.getParameterTypes();
                Field[] fields = impl.getDeclaredFields();
                return impl.getDeclaredConstructor(params).newInstance(getDependencyInstance(params, fields));
            }
        }
        return null;
    }

    private Object[] getDependencyInstance(Class<?>[] param, Field[] fields) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?>[] dependency = getDependency(param, fields);
        List<Object> objectList = new ArrayList<>();
        Arrays.sort(dependency);
        Arrays.sort(param);
        if (Arrays.equals(dependency, param)) {
            for (Class<?> c : dependency) {
                Object o = c.getDeclaredConstructor().newInstance();
                objectList.add(o);
            }
        } else {
            throw new BindingNotFoundException("");

        }
        return objectList.toArray();
    }

    private Class<?>[] getDependency(Class<?>[] param, Field[] fields) {
        Class<?>[] dep = new Class<?>[param.length];
        int index = 0;
        for (Class<?> clazz : param) {
            for (Field field : fields) {
                if (clazz.getName().equals(field.getType().getName())) {
                    dep[index] = field.getType();
                    index++;
                }
            }
        }
        return dep;
    }

    private boolean validCountAnnotation(Constructor<?>[] constructors) {
        return countAnnotation(constructors) < 2;
    }

    private int countAnnotation(Constructor<?>[] constructors) {
        return (int) Arrays.stream(constructors)
                .filter(s -> s.isAnnotationPresent(Inject.class))
                .count();
    }

}
