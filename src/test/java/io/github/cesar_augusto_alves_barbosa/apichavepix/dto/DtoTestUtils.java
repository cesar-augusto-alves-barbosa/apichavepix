package io.github.cesar_augusto_alves_barbosa.apichavepix.dto;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import static org.junit.jupiter.api.Assertions.*;

public class DtoTestUtils {

    public static <T> void testDto(Class<T> dtoClass, Object... constructorArgs) {
        try {
            Constructor<?>[] constructors = dtoClass.getDeclaredConstructors();
            assertTrue(constructors.length > 0, "Nenhum construtor encontrado para " + dtoClass.getSimpleName());

            Constructor<T> constructor = (Constructor<T>) constructors[0];
            constructor.setAccessible(true);
            T dtoInstance = constructor.newInstance(constructorArgs);

            assertNotNull(dtoInstance, "A instância do DTO não deve ser nula");
            assertEquals(dtoInstance, dtoInstance, "O DTO deve ser igual a si mesmo");
            assertEquals(dtoInstance.hashCode(), dtoInstance.hashCode(), "O hashCode deve ser consistente");

            assertNotNull(dtoInstance.toString(), "O método toString não deve retornar nulo");
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            fail("Erro ao instanciar DTO: " + e.getMessage());
        }
    }
}
