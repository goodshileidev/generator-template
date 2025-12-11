import deepClone from './deepClone';

/**
 * 差异详情
 */
interface DiffDetail {
  old: any;
  new: any;
}

/**
 * 差异数据
 */
interface DiffData {
  deepType?: string;
  type?: string;
  detail?: DiffDetail | DiffData;
}

/**
 * 对比结果
 */
interface Result {
  readonly type: string;
  data: DiffData;
  success: boolean;
  errorMsg?: string;
  isChange: boolean;
}

/**
 * 获取引用数据具体类型字符串
 * @param obj 对象
 * @returns {string}
 */
const getInstance = (obj: any): string => {
  if (typeof obj !== 'object' && typeof obj !== 'function') return typeof obj;
  const _toString = Object.prototype.toString;
  return _toString.call(obj).slice(8, -1);
};

/**
 * 判断两个对象是否属于相同的类型
 * @param obj1 对象1
 * @param obj2 对象2
 * @returns
 */
const equalType = (obj1: any, obj2: any): boolean => {
  if (typeof obj1 !== 'object' && typeof obj1 !== 'function') return false;
  return getInstance(obj1) === getInstance(obj2);
};

const diffObject = (result: any, payload1: any, payload2: any): boolean => {
  let change = false;
  const _obj2 = deepClone(payload2);
  for (const key in payload1) {
    if (payload1.hasOwnProperty && payload1.hasOwnProperty(key)) {
      //  对象2不存在该属性则代表删除了该属性
      if (!_obj2.hasOwnProperty(key)) {
        result[key] = {
          type: 'delete',
          detail: {
            old: payload1[key],
            new: null,
          },
        };
        change = true;
        continue;
      }
      // 数据结构相同且不为基本数据类型时
      if (equalType(payload1[key], payload2[key])) {
        result[key] = result[key] ? result[key] : {};
        change = change || diffObject(result[key], payload1[key], payload2[key]);
        delete _obj2[key];
        continue;
      }
      // 数据类型为字符串
      if (
        payload1[key] !== payload2[key] &&
        typeof payload1[key] === 'string' &&
        typeof payload2[key] === 'string' &&
        (payload1[key].length > 1 || payload2[key].length > 1)
      ) {
        result[key] = result[key]
          ? result[key]
          : {
              deepType: 'String',
              type: 'modify',
              detail: { old: payload1[key], new: payload2[key] },
            };
        delete _obj2[key];
        continue;
      }

      // 基本类型直接比较是否不同
      if (payload2[key] && payload1[key] !== payload2[key]) {
        result[key] = {
          type: 'modify',
          detail: {
            old: payload1[key],
            new: payload2[key],
          },
        };
        change = true;
        // 删除对应json2上的key
        delete _obj2[key];
        continue;
      }
      // 相同情况，删除json2中的key
      delete _obj2[key];
    }
  }

  // json2剩余的均为新增
  for (const key in _obj2) {
    if (_obj2.hasOwnProperty && _obj2.hasOwnProperty(key)) {
      change = true;
      result[key] = {
        type: 'add',
        detail: {
          old: null,
          new: payload2[key],
        },
      };
    }
  }
  return change;
};

/**
 * json对比
 * @param payload1 对象1
 * @param payload2 对象2
 * @returns
 */
const jsonDiff = (payload1: any[] | any, payload2: any[] | any): Result => {
  const result: Result = {
    type: getInstance(payload2),
    data: {},
    success: true,
    isChange: false
  };
  if (typeof payload1 !== typeof payload2) {
    result.success = false;
    result.errorMsg = '数据基本类型不同';
    return result;
  }
  //  判断引用类型不同
  if (!equalType(payload1, payload2)) {
    result.success = false;
    result.errorMsg = '数据类型不同';
    return result;
  }

  result.isChange = diffObject(result.data, payload1, payload2);
  return result;
};

export default jsonDiff;
