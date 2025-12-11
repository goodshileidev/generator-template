const extractConfig = {
  "page": {
    "pageName": "工单查询",
    "pageKey": "WorkOrderQuery",
    "pageNo": "1.1.2.1",
    "basicDescription": "在客户服务过程中记录工单记录，进行查询、处理、跟进等操作",
    "scopeType": "module",
    "pageType": "main_page",
    // "pageLayoutTemplateNo": "template1",
    // "dataSrcTableList": [
    //   "WorkOrderTable"
    // ],
    // "programApiList": [
    //   "WorkOrderAPI"
    // ],
    // "parentPageNo": "1.1",
    // "childPageList": [
    //   "1.1.2.2"
    // ],
    // "showConditionScript": "PublicWorkflowScript",
    // "showConditionList": [
    //   "查看权限",
    //   "查询条件"
    // ]
  },
  "pageField": {
    "pageFieldName": "部门",
    "pageFieldNo": "1",
    "pageFieldKey": "Department",
    "dataType": "String",
    "pageId": 1,
    // "listConditionScript": "if(role in ['manager'])",
    // "listConditionList": {
    //   "role": "manager"
    // },
    // "editConditionScript": "editable == true",
    // "editConditionList": {
    //   "editable": true
    // },
    "componentType": "Dropdown",
    // "entityName": "WorkOrder",
    // "tableName": "DepartmentTable",
    // "entityFieldName": "DepartmentName",
    // "columnName": "dept_name",
    // "basicDescription": "选择用户所在的部门",
    // "programApiList": {
    //   "apis": [
    //     "getDepartments"
    //   ]
    // }
  },
  "entity": {
    "entityName": "Work Order",
    "entityKey": "work_order",
    "conceptualDescription": "Represents a record of tasks and processes in customer service within the system.",
    "logicalDescription": "Includes various attributes such as ID, client name, order name, steps, status, and other related details. It can be queried and managed through different permissions and rules.",
    "encodingStr": "UTF-8",
    "collationStr": "utf8_general_ci",
    // "primaryKeyGenerateMethod": "Auto Increment",
    // "primaryKeyGeneratorName": "WorkOrderIDGenerator"
  },
  "pageTable": {
    "table": {
      "pageFieldName": "工单查询",
      "pageId": 37,
      "pageFieldNo": "F001",
      "pageFieldKey": "work_order_query",
      "dataType": "varchar",
      "listConditionScript": "",
      "listConditionList": [],
      "editConditionScript": "",
      "editConditionList": [],
      "componentType": "table",
      "entityName": "工单",
      "tableName": "work_order",
      "entityFieldName": "query",
      "columnName": "query",
      "basicDescription": "用于查询工单信息",
      "programApiList": []
    },
    "column": [
      {
        "pageFieldName": "工单编号",
        "pageId": 37,
        "pageFieldNo": "F002",
        "pageFieldKey": "work_order_number",
        "dataType": "char",
        "listConditionScript": "",
        "listConditionList": [],
        "editConditionScript": "",
        "editConditionList": [],
        "componentType": "tableColumn",
        "entityName": "工单",
        "tableName": "work_order",
        "entityFieldName": "number",
        "columnName": "number",
        "basicDescription": "记录工单的唯一编号",
        "programApiList": []
      }
    ]
  }
}

export default extractConfig;
