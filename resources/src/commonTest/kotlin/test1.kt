import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.resources.test.createStringResourceMock
import kotlin.test.Test
import kotlin.test.assertTrue

class StringDescBasicTest {
    @Test
    fun resource_returns_a_resource_desc() {
        val hello = createStringResourceMock("Hello from mock")

        val desc: StringDesc = hello.desc()

        // 不要 assertEquals("xxx", desc.toString())
        // 只要断言它是“资源型描述”即可（不同版本可能没有公开类型，就用 toString 包含判断）
        assertTrue(desc.toString().contains("ResourceStringDesc") || desc.toString().contains("StringResource"))
    }
}
