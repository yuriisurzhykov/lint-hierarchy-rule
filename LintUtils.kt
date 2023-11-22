import com.intellij.psi.PsiClass

internal fun PsiClass.implements(
    qualifiedName: String,
    nameFilter: (String) -> Boolean = { true }
): Boolean {
    val fqcn = this.qualifiedName ?: return false
    if (fqcn == qualifiedName) {
        // Found a match
        return true
    }

    if (!nameFilter(fqcn)) {
        // Don't proceed further
        return false
    }

    return this.superTypes.filterNotNull().any { classType ->
        classType.resolve()?.implements(qualifiedName, nameFilter) ?: false
    }
}

internal fun PsiClass.overrides(methodName: String): Boolean {
   return methods.any {
       it.name == methodName
   }
}
