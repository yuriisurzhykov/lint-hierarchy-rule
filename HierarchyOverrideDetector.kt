
import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.yuriisurzhykov.statemachine.lint.utils.implements
import com.yuriisurzhykov.statemachine.lint.utils.overrides
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement

class HierarchyOverrideDetector : Detector(), SourceCodeScanner {

    private val hierarchicalStateName = "com.niceforyou.yuriisurzhykov.State.Transition"
    private val overrideRequiredMethodName = "hasInitialTransition"

    override fun getApplicableUastTypes(): List<Class<out UElement>> {
        return listOf(UClass::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            override fun visitClass(node: UClass) {
                if (node.isEnum) return
                val extendsHierarchicalState =
                    node.implements(hierarchicalStateName)

                val implementsHasInitialTransition = node.overrides(overrideRequiredMethodName)

                if (extendsHierarchicalState && !implementsHasInitialTransition) {
                    context.report(
                        OVERRIDE_ISSUE,
                        node,
                        context.getLocation(element = node),
                        "Class is inherited from State.Transition but does not override hasInitialTransition",
                        quickfixData = fix().data().autoFix()
                    )
                }
            }
        }
    }

    companion object {
        val OVERRIDE_ISSUE: Issue = Issue.create(
            id = "OverrideRequired",
            briefDescription = "Inherits but not overrides",
            explanation = """
                Classes, derived from State.Hierarchical, have to override hasInitialTransition function.
            """,
            category = Category.SECURITY,
            priority = 100,
            severity = Severity.FATAL,
            implementation = Implementation(
                HierarchyOverrideDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}
