import React from "react";
import {Form, Select} from 'antd'

interface SearchConditionSelectProps {
  decentKeys: string
}


const SearchConditionSelect: React.FC<SearchConditionSelectProps> = ((props) => {
  const form = Form.useFormInstance();
  const key = props.id.replace("Condition", "")
  return <Select
    showSearch
    optionFilterProp="label"
    allowClear={true}
    onClear={() => {
      console.debug("onClear")
      // const key = props.key
      let globalSearchCondition = window.localStorage.getItem("globalSearchCondition")
      console.debug("onClear", globalSearchCondition)
      if (!globalSearchCondition) {
        return
      } else {
        globalSearchCondition = JSON.parse(globalSearchCondition)
      }
      // for (const idx in props.keys) {
      //   const key = props.keys[idx]
      //   delete globalSearchCondition[key]
      //   delete globalSearchCondition[key + "Option"]
      // }
      delete globalSearchCondition[key]
      delete globalSearchCondition[key + "Option"]
      form.setFieldValue(key, null)
      if (props.decentKeys) {
        for (const idx in props.decentKeys) {
          const key = props.decentKeys[idx]
          delete globalSearchCondition[key]
          delete globalSearchCondition[key + "Option"]
          form.setFieldValue(key + "Condition", null)
          console.debug("clear:" + key)
        }
      }
      window.localStorage.setItem("globalSearchCondition", JSON.stringify(globalSearchCondition))
    }}
    onSelect={(value, option) => {
      console.debug("onSelect", value, option)
      // const key = props.key
      let globalSearchCondition = window.localStorage.getItem("globalSearchCondition")
      if (!globalSearchCondition) {
        globalSearchCondition = {}
      } else {
        globalSearchCondition = JSON.parse(globalSearchCondition)
      }
      globalSearchCondition[key] = value
      globalSearchCondition[key + "Option"] = option
      window.localStorage.setItem("globalSearchCondition", JSON.stringify(globalSearchCondition))
    }}

    defaultValue={() => {
      // const key = props.key
      let globalSearchCondition = window.localStorage.getItem("globalSearchCondition")
      if (globalSearchCondition) {
        globalSearchCondition = JSON.parse(globalSearchCondition)
        let option = globalSearchCondition[key + "Option"]
        console.debug("defaultValue", option)
        form.setFieldValue(key + "Condition", globalSearchCondition[key])
        return option
      } else {
        return null
      }
    }}
    {...props}>
  </Select>
})

export default SearchConditionSelect;
