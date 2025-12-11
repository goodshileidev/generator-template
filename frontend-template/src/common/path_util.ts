export function getPathParam(location: string, params: string[]) {
  const path_strs = location.split("/")
  const result = {}
  for (let i = 0; i < params.length; i++) {
    result[params[i]] = path_strs[path_strs.length - params.length + i]
  }
  return result
}


export function generateUUID() {
  const array = new Uint32Array(4);
  window.crypto.getRandomValues(array);
  return Array.from(array, value => value.toString(16).padStart(8, '0')).join('-');
}
