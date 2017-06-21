import com.pwr.zpi.io.Configuration
import com.pwr.zpi.language.Formula
import org.testng.annotations.Test

/**
 * Created by Mateo on 21.06.2017.
 */
class ConfigurationTest extends GroovyTestCase {

    @Test
    void test() {
        assertEquals 3.0, Configuration.MAX_THRESHOLD
        assertEquals '#', Configuration.COMMENT_SIGN
        assertEquals "config/scenarios/", Configuration.SCENARIOS_DIR
        assertEquals 5, Configuration.LATEST_GROUP_SIZE
        assertEquals true, Configuration.OVERRIDE_IF_EXISTS

        assert Configuration.MIN_POS instanceof Double
        assert Configuration.COMMENT_SIGN instanceof Character
        assert Configuration.MAIN_MODULE_NAME instanceof String
        assert Configuration.LISTENING_SERVER_PORT instanceof Integer
        assert Configuration.DK_IS_COMPLEX instanceof Boolean

        def thresholds = Configuration.getThresholds(Formula.Type.SIMPLE_MODALITY)

        assertNotNull thresholds

        assertEquals 0.2, thresholds[0]
        assertEquals 0.6, thresholds[1]
        assertEquals 0.7, thresholds[2]
        assertEquals 0.9, thresholds[3]
        assertEquals 1.0, thresholds[4]
    }
}
