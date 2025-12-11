import {listEntity} from "@/common/service/data/entity"
import {listEntityField} from "@/common/service/data/entity_field"


import {listPageLayoutTemplateSection} from "@/common/service/system/page_layout_template_section"


import {listReportLayoutTemplateSection} from "@/common/service/system/report_layout_template_section"


import {listPageVar} from "@/common/service/ui/page_var"
import {listPage} from "@/common/service/ui/page"

import {listPageAction} from "@/common/service/ui/page_action"

import {listPageField} from "@/common/service/ui/page_field"

import {listPageFieldAction} from "@/common/service/ui/page_field_action"


import {listReportIndicator} from "@/common/service/report/report_indicator"


import {listModule} from "@/common/service/management/module"
import {listProgramSql} from "@/common/service/program/program_sql"

import {listTestRule} from "@/common/service/test/test_rule"
import {listTestRuleItem} from "@/common/service/test/test_rule_item"


import {listCustomComponent} from "@/common/service/system/custom_component"


import {listProject} from "@/common/service/management/project"

import {listCodeEnum} from "@/common/service/data/code_enum"


import {listFunction} from "@/common/service/management/function"


import {listDevCycle} from "@/common/service/management/dev_cycle"


import {listSystem} from "@/common/service/program/system"

import {listReportLayoutTemplate} from "@/common/service/system/report_layout_template"


import {listReport} from "@/common/service/report/report"

import {listTestCase} from "@/common/service/test/test_case"

import {listTestScript} from "@/common/service/test/test_script"


import {listPageLayoutSection} from "@/common/service/ui/page_layout_section"

import {listPageLayoutTemplate} from "@/common/service/system/page_layout_template"

import {listPageSection} from "@/common/service/ui/page_section"
import {listPageLayout} from "@/common/service/ui/page_layout"


import {listUserDocument} from "@/common/service/demand/user_document"

import {listUserRequirement} from "@/common/service/demand/user_requirement"


import {listBizFlow} from "@/common/service/demand/biz_flow"

import {listInternalApi} from "@/common/service/program/internal_api"
import {listExternalApi} from "@/common/service/program/external_api";

interface ItemProps {
  label: string
  value: string
  data: any
}

export const codeListService = {
  getCodeListEntity: async (params: any) => {
    const response = await listEntity(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.entityId,
        label: data.entityName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListEntityField: async (params: any) => {
    const response = await listEntityField(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.entityFieldId,
        label: data.entityFieldName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListPageLayoutTemplateSection: async (params: any, keyField = "pageLayoutTemplateSectionId") => {
    const response = await listPageLayoutTemplateSection(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data[keyField],
        label: data.pageLayoutTemplateSectionName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListReportLayoutTemplateSection: async (params: any) => {
    const response = await listReportLayoutTemplateSection(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.reportLayoutTemplateSectionKey,
        label: data.reportLayoutTemplateSectionName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListPageVar: async (params: any)=> {
    const response = await listPageVar(params)
    const codeList: ItemProps[]=[]
    const list=[]
    if (response.list) {
      list.push(...response.list);    } else if (response.data && response.data.list) {
      list.push(...response.data.list);    } else if (response.data) {
      list.push(...response.data);    }
    list.forEach((data: any)=> {
       codeList.push({
         value: data.pageVarId,
         label: data.pageVarName,
         data: data
      })
    })
    return codeList;
  },
  getCodeListPage: async (params: any) => {
    const response = await listPage(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.pageId,
        label: data.pageName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListPageAction: async (params: any) => {
    const response = await listPageAction(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.pageActionId,
        label: data.pageActionName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListPageField: async (params: any) => {
    const response = await listPageField(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.pageFieldId,
        label: data.pageFieldName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListPageFieldAction: async (params: any) => {
    const response = await listPageFieldAction(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.pageFieldActionId,
        label: data.pageFieldActionName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListReportIndicator: async (params: any) => {
    const response = await listReportIndicator(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.reportIndicatorId,
        label: data.reportIndicatorName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListModule: async (params: any) => {
    const response = await listModule(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.moduleId,
        label: data.moduleName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListProgramSql: async (params: any) => {
    const response = await listProgramSql(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.programSqlNo,
        label: data.programSqlName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListTestRule: async (params: any) => {
    const response = await listTestRule(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.testRuleId,
        label: data.testRuleName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListTestRuleItem: async (params: any) => {
    const response = await listTestRuleItem(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.testRuleItemId,
        label: data.testRuleItemName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListCustomComponent: async (params: any)=> {
    const response = await listCustomComponent(params)
    const codeList: ItemProps[]=[]
    const list=[]
    if (response.list) {
      list.push(...response.list);    } else if (response.data && response.data.list) {
      list.push(...response.data.list);    } else if (response.data) {
      list.push(...response.data);    }
    list.forEach((data: any)=> {
       codeList.push({
         value: data.customComponentKey,
         label: data.customComponentName,
         data: data
      })
    })
    return codeList;
  },
  getCodeListProject: async (params: any) => {
    const response = await listProject(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.projectId,
        label: data.projectName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListCodeEnum: async (params: any)=> {
    const response = await listCodeEnum(params)
    const codeList: ItemProps[]=[]
    const list=[]
    if (response.list) {
      list.push(...response.list);    } else if (response.data && response.data.list) {
      list.push(...response.data.list);    } else if (response.data) {
      list.push(...response.data);    }
    list.forEach((data: any)=> {
       codeList.push({
         value: data.codeEnumId,
         label: data.codeEnumName,
         data: data
      })
    })
    return codeList;
  },
getCodeListFunction: async (params: any)=> {
    const response = await listFunction(params)
    const codeList: ItemProps[]=[]
    const list=[]
    if (response.list) {
      list.push(...response.list);    } else if (response.data && response.data.list) {
      list.push(...response.data.list);    } else if (response.data) {
      list.push(...response.data);    }
    list.forEach((data: any)=> {
       codeList.push({
         value: data.functionId,
         label: data.functionName,
         data: data
      })
    })
    return codeList;
  },
getCodeListDevCycle: async (params: any)=> {
    const response = await listDevCycle(params)
    const codeList: ItemProps[]=[]
    const list=[]
    if (response.list) {
      list.push(...response.list);    } else if (response.data && response.data.list) {
      list.push(...response.data.list);    } else if (response.data) {
      list.push(...response.data);    }
    list.forEach((data: any)=> {
       codeList.push({
         value: data.devCycleId,
         label: data.devCycleName,
         data: data
      })
    })
    return codeList;
  },
  getCodeListSystem: async (params: any) => {
    const response = await listSystem(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.systemId,
        label: data.systemName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListBizFlow: async (params: any) => {
    const response = await listBizFlow(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.bizFlowId,
        label: data.bizFlowName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListReportLayoutTemplate: async (params: any) => {
    const response = await listReportLayoutTemplate(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.reportLayoutTemplateNo,
        label: data.reportLayoutTemplateName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListReport: async (params: any) => {
    const response = await listReport(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.reportId,
        label: data.reportName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListFunction: async (params: any) => {
    const response = await listFunction(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.functionId,
        label: data.functionName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListTestCase: async (params: any) => {
    const response = await listTestCase(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.testCaseId,
        label: data.testCaseName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListTestScript: async (params: any) => {
    const response = await listTestScript(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.testScriptId,
        label: data.testScriptName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListPageLayoutSection: async (params: any) => {
    const response = await listPageLayoutSection(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.pageLayoutSectionNo,
        label: data.pageLayoutSectionName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListPageLayoutTemplate: async (params: any) => {
    const response = await listPageLayoutTemplate(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.pageLayoutTemplateNo,
        label: data.pageLayoutTemplateName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListPageSection: async (params: any) => {
    const response = await listPageSection(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.pageSectionId,
        label: data.pageSectionName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListPageLayout: async (params: any) => {
    const response = await listPageLayout(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.pageLayoutId,
        label: data.pageLayoutName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListUserDocument: async (params: any) => {
    const response = await listUserDocument(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.userDocumentId,
        label: data.userDocumentName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListUserRequirement: async (params: any) => {
    const response = await listUserRequirement(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.userRequirementId,
        label: data.userRequirementName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListInternalApi: async (params: any) => {
    const response = await listInternalApi(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      data.apiName = data.internalApiName
      data.apiId = data.internalApiId
      data.isInternal = true
      codeList.push({
        value: data.internalApiId,
        label: data.internalApiName,
        data: data
      })
    })
    return codeList;
  },

  getCodeListExternalApi: async (params: any) => {
    const response = await listExternalApi(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      data.apiName = data.externalApiName
      data.apiId = data.externalApiId
      data.isInternal = false
      codeList.push({
        value: data.externalApiId,
        label: data.externalApiName,
        data: data
      })
    })
    return codeList;
  },

  getCodeListBizFlow: async (params: any) => {
    const response = await listBizFlow(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.bizFlowId,
        label: data.bizFlowName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListCodeEnum: async (params: any) => {
    const response = await listCodeEnum(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.codeEnumId,
        label: data.codeEnumName,
        data: data
      })
    })
    return codeList;
  },
  getCodeListDevCycle: async (params: any) => {
    const response = await listDevCycle(params)
    const codeList: ItemProps[] = []
    const list = []
    if (response.list) {
      list.push(...response.list);
    } else if (response.data && response.data.list) {
      list.push(...response.data.list);
    } else if (response.data) {
      list.push(...response.data);
    }
    list.forEach((data: any) => {
      codeList.push({
        value: data.devCycleId,
        label: data.devCycleName,
        data: data
      })
    })
    return codeList;
  },
}
