export function isSearchFieldSet(fieldName) {
  let globalSearchCondition = window.localStorage.getItem("globalSearchCondition")
  if (!globalSearchCondition) {
    globalSearchCondition = {}
  } else {
    globalSearchCondition = JSON.parse(globalSearchCondition)
  }
  return globalSearchCondition[fieldName]
}
