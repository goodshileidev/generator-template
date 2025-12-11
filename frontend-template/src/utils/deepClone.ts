const hasOwnProp = (obj: any, key: string) => {
  return obj && obj.hasOwnProperty ? obj.hasOwnProperty(key) : false;
};

const getCativeCtor = (val: any, args?: any) => {
  const Ctor = val.__proto__.constructor;
  return args ? new Ctor(args) : new Ctor();
};

const copyValue = (val: any) => {
  if (val) {
    switch (Object.prototype.toString.call(val)) {
      case '[object Object]': {
        const restObj = Object.create(Object.getPrototypeOf(val));
        for (const key in val) {
          if (hasOwnProp(val, key)) {
            restObj[key] = copyValue(val[key]);
          }
        }
        return restObj;
      }
      case '[object Date]':
      case '[object RegExp]': {
        return getCativeCtor(val, val.valueOf());
      }
      case '[object Array]':
      case '[object Arguments]': {
        const restArr: any[] = [];
        if (val.forEach) {
          val.forEach((item: any) => {
            restArr.push(copyValue(item));
          });
        } else {
          for (let i = 0; i < val.length; i++) {
            restArr.push(copyValue(val[i]));
          }
        }
        return restArr;
      }
      case '[object Set]': {
        const restSet: Set<any> = getCativeCtor(val);
        restSet.forEach(function (item) {
          restSet.add(copyValue(item));
        });
        return restSet;
      }
      case '[object Map]': {
        const restMap: Map<any, any> = getCativeCtor(val);
        restMap.forEach(function (item, key) {
          restMap.set(key, copyValue(item));
        });
        return restMap;
      }
    }
  }
  return val;
};

//  深拷贝
const deepClone = <T>(obj: T): T => {
  if (obj) {
    return copyValue(obj);
  }
  return obj;
};

export default deepClone;
