import {Form, RefSelectProps, Select, SelectProps} from 'antd';
import React, {useEffect, useState} from 'react';

export type NamePath = (string | number)[];

//  匹配下拉选项的后缀，只有当满足以该数组中的任意一个值为后缀才会进行自动填充
const MATCH_KEY_PATTERNS = ['key', 'id', 'no'] as const;
//  满足自动填充时，需要填充的字段后缀
const FIELD_NAME_PATTERNS = ['name', 'type', 'detail', 'no', 'key', 'title', 'label', 'code'] as const;

export type RelationMappingType = {
  [k in (typeof FIELD_NAME_PATTERNS)[number]]?: string;
};

export type DynamicSelectProps = React.PropsWithChildren<SelectProps> &
  React.RefAttributes<RefSelectProps> & {
  fieldPath?: NamePath,
  fieldName?: string,
  optionDataKey?: string,
  optionFieldPrefix?: string,
  fuzzy?: false,
  listIndex?: number,
  filterOpt?: 'hidden' | 'disabled',
  relationMapping?: RelationMappingType,
  fieldMapping?: any,
  filterOption: any,
  options: any,
  getOptionsCallback?: any
};

const getRelationMap = (
  keyFieldName?: string,
  optionFieldPrefix?: string,
  relationMapping?: RelationMappingType,
): {
  fieldNamePrefix: string;
  fieldNameMap: {
    optionFieldName: string;
    formFieldName: string;
  }[];
} | null => {
  if (keyFieldName === '' || keyFieldName === null || typeof keyFieldName === 'undefined') {
    return null;
  }
  for (const pattern of MATCH_KEY_PATTERNS) {
    if (keyFieldName.toLowerCase().endsWith(`_${pattern}`)) {
      const fieldNamePrefix = keyFieldName.slice(0, keyFieldName.length - pattern.length - 1);
      const optionFieldNamePrefix = optionFieldPrefix || fieldNamePrefix
      return {
        fieldNamePrefix: fieldNamePrefix,
        fieldNameMap: FIELD_NAME_PATTERNS.map((keyPattern) => ({
          formFieldName: `${fieldNamePrefix}_${keyPattern}`,
          optionFieldName: relationMapping && relationMapping[keyPattern] ? relationMapping[keyPattern] : `${optionFieldNamePrefix}_${keyPattern}`,
        })),
      };
    }
    if (keyFieldName.toLowerCase().endsWith(pattern)) {
      const fieldNamePrefix = keyFieldName.slice(0, keyFieldName.length - pattern.length);
      const optionFieldNamePrefix = optionFieldPrefix || fieldNamePrefix
      return {
        fieldNamePrefix: fieldNamePrefix,
        fieldNameMap: FIELD_NAME_PATTERNS.map((keyPattern) => ({
          formFieldName: `${fieldNamePrefix}${keyPattern.charAt(0).toUpperCase() + keyPattern.slice(1)}`,
          optionFieldName:
            relationMapping && relationMapping[keyPattern]
              ? relationMapping[keyPattern]
              : `${optionFieldNamePrefix}${keyPattern.charAt(0).toUpperCase() + keyPattern.slice(1)}`,
        })),
      };
    }
  }
  return null;
};

const DynamicSelect: React.FC<DynamicSelectProps> = ({
                                                       fieldPath,
                                                       fieldName,
                                                       optionDataKey,
                                                       fuzzy,
                                                       optionFieldPrefix,
                                                       options,
                                                       filterOpt,
                                                       listIndex,
                                                       relationMapping,
                                                       fieldMapping,
                                                       getOptionsCallback,
                                                       filterOption,
                                                       ...props
                                                     }) => {
    const form = Form.useFormInstance();
    const fieldRelationMap = getRelationMap(fieldName, optionFieldPrefix, relationMapping);

    optionDataKey = optionDataKey || 'data';

    const isFuzzy = (!(typeof fuzzy === "undefined") && fuzzy !== false);

    const parentIsList = typeof listIndex === 'number';

    const needFilter = filterOpt === 'hidden' || filterOpt === 'disabled';

    fieldPath = fieldPath && fieldPath.length > 0 ? fieldPath : [];

    const [optionList, setOptionList] = useState<any[]>(options)

    const getSelectKeys = (): string[] => {
      if (fieldPath && fieldName) {
        const selectKeys: string[] = [];
        (form.getFieldValue(fieldPath) || []).forEach((item: any, index: number) => {
          if (
            item &&
            item.hasOwnProperty(fieldName) &&
            ((parentIsList && listIndex !== index) || !parentIsList)
          ) {
            selectKeys.push(item[fieldName]);
          }
        });
        return selectKeys;
      }
      return [];
    };

    const getOptions = (optionList) => {
      if (needFilter) {
        const selectKeys = getSelectKeys();
        const filterOptions: any[] = [];
        (options || optionList || []).forEach((opt) => {
          if (selectKeys.findIndex((i) => i === opt.value) !== -1) {
            if (filterOpt === 'disabled') {
              filterOptions.push({
                ...opt,
                disabled: true,
              });
            }
          } else {
            filterOptions.push(opt);
          }
        });
        console.debug("DynamicSelect[" + fieldName + "]->filterOptions", selectKeys, options)
        return filterOptions;
      } else {
        console.debug("DynamicSelect[" + fieldName + "]->getOptions", options)
        return optionList;
      }
    };

    const handleRelation = (option: any) => {
      if (!fieldName || !fieldRelationMap) {
        return;
      }

      const sourceData = option[optionDataKey];
      console.debug("handleRelation,option=", option)
      console.debug("handleRelation,sourceData=", sourceData)
      console.debug("handleRelation,relation=", fieldRelationMap)
      console.debug("handleRelation,relationMapping=", relationMapping)
      console.debug("handleRelation,optionDataKey=", optionDataKey)
      if (!sourceData) {
        console.log('没有获取到选项中的源数据');
        return;
      }
      if (isFuzzy) {
        Object.keys(sourceData).forEach((sourceDataFieldName) => {
          fieldRelationMap.fieldNameMap.forEach((mapEntry) => {
            const {optionFieldName, formFieldName} = mapEntry;
            if (sourceDataFieldName.toLowerCase().endsWith(optionFieldName.toLowerCase())) {
              //  模糊匹配时，如果设置了映射关系值则绑定到映射关系上，否则绑定在模糊匹配字段上
              const path = [...fieldPath, ...typeof listIndex === 'number' ? [listIndex] : [], formFieldName]
              const value = sourceData[optionFieldName]
              console.debug("setFieldValue", path, value)
              form.setFieldValue(path, value);
            }
          });
        });
      } else {
        fieldRelationMap.fieldNameMap.forEach((mapEntry) => {
          const {optionFieldName, formFieldName} = mapEntry;
          const path = [...fieldPath, ...typeof listIndex === 'number' ? [listIndex] : [], formFieldName]
          const value = sourceData[optionFieldName]
          console.debug("setFieldValue", path, value)
          //  全等匹配时，按照映射关系来
          form.setFieldValue(path, value);
        });
      }
      if (fieldMapping) {
        for (let idx in fieldMapping) {
          const optionFieldName = idx;
          const formFieldName = fieldMapping[optionFieldName];
          const path = [...fieldPath, ...typeof listIndex === 'number' ? [listIndex] : [], formFieldName]
          const value = sourceData[optionFieldName]
          console.debug("setFieldValue", path, value)
          //  全等匹配时，按照映射关系来
          form.setFieldValue(path, value);
        }
      }
    };

    const handleSelectChange: SelectProps['onSelect'] = (value, option) => {
      handleRelation(option);
      if (props.onChange && typeof props.onChange === 'function') {
        props.onChange(value, option);
      }
    };

    useEffect(() => {
        if (getOptionsCallback) {
          getOptionsCallback({}).then((list) => {
            const options = getOptions(list)
            setOptionList(options)
          })
        } else if (options) {
          const optionList = getOptions(options)
          setOptionList(optionList)
        }
      }, [options, getOptionsCallback]
    )
    return (
      <>
        <Select
          showSearch
          optionFilterProp="label"
          {...props}
          options={optionList}
          filterOption={filterOption}
          onSelect={handleSelectChange}></Select>
      </>
    );
  }
;

export default DynamicSelect;
