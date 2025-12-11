export function safeParseJson(jsonStr) {
  if (!jsonStr || jsonStr === "") {
    return null
  }
  if (jsonStr === "\"[]\"") {
    return []
  }
  if (jsonStr === "\"{}\"") {
    return {}
  }
  let result = JSON.parse(jsonStr)
  if (typeof result === "string") {
    result = JSON.parse(result)
  }
  // if (typeof result === "string") {
  //   await new Promise(resolve => setTimeout(resolve, 1000));
  // }
  return result
}
