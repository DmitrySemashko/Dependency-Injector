import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.lang.reflect.InvocationTargetException;


public class InjectorTest {
    @Test
    public void testMethod() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Injector injector = new InjectorImpl();
        injector.bind(DogService.class,DogServiceImpl.class);
        Provider<DogService> dogServiceProvider = injector.getProvider(DogService.class);
        Provider <DogService> provider = injector.getProvider(DogService.class);

        assertNotNull(DogService.class);
        assertNotNull(dogServiceProvider.getInstance());
        assertSame(DogServiceImpl.class,dogServiceProvider.getInstance().getClass());
        assertSame(provider.getInstance(),dogServiceProvider.getInstance());
    }
}
