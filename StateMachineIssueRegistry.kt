import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue
import com.google.auto.service.AutoService

@Suppress("unused")
@AutoService(value = [IssueRegistry::class])
class StateMachineIssueRegistry : IssueRegistry() {
    override val issues: List<Issue>
        get() = listOf(
            HierarchyOverrideDetector.OVERRIDE_ISSUE
        )

    override val api: Int
        get() = CURRENT_API

    override val minApi: Int
        get() = 7

    override val vendor: Vendor
        get() = Vendor("Yurii Surzhykov", "com.yuriisurzhykov")
}
