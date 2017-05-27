package context

import com.pwr.zpi.holons.context.FilteringContext
import com.pwr.zpi.holons.context.LatestFilteringContext
import com.pwr.zpi.holons.context.measures.Distance
import com.pwr.zpi.holons.context.measures.Measure

/**
 * Created by Grzesiek on 2017-05-27.
 */
class LatestFilteringContextTest extends GroovyTestCase {
    FilteringContext testObj;
    Measure distance;

    void setUp() {
        distance = new Distance();
        testObj = new LatestFilteringContext()
    }

    void tearDown() {
    }

    void testSelectRepresentativeBPs() {
    }

    void testPerformContextualisation() {
    }

    void testIsMeetingCondition() {
    }

    void testDetermineFulfillment() {

    }

    void testSelectRepresentativeBPs1() {
    }
}
