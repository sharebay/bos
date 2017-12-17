
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class AreaDaoTest {

    @Test
    public void test02() {
        int a = 1 + '1';
        assert (a==2);
    }

    @Test
    public void test03() {
        ArrayList<String> strings = new ArrayList<>(Arrays.asList("aaa", "bbb", "ccc", "ddd"));

        System.out.println(strings);
    }
    
    @Test
    public void testLambda() {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1,3,4,5,7,3,2,6,8,9,1,5));
        list.stream()
                .filter(i -> i > 5)
                .forEach(i -> System.out.println(i));
    }

}