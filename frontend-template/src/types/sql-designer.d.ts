/**
 * 数据库表列
 */
export interface SqlTableColumn {
  //  列名称
  name: string;
  //  列类型
  type: string;
  //  描述
  comment: string;
  //  原因
  reason: string;
  //  是否开启过滤
  selected?: boolean;
}

/**
 * 数据库表
 */
export interface SqlTable {
  //  表名
  name: string;
  //  描述
  comment: string;
  //  表格列
  columns: SqlTableColumn[];
  //  是否开启过滤
  selected?: boolean;
}

/**
 * GroupBy SQL片段
 */
export interface GroupBySqlFragment {
  annotation: string;
  fragment: {
    column: string;
    table: string;
  };
}

/**
 * OrderBy SQL片段
 */
export interface OrderBySqlFragment {
  annotation: string;
  fragment: {
    column: string;
    table: string;
    type: 'asc' | 'desc';
  };
}

/**
 * Select SQL片段
 */
export interface SelectSqlFragment {
  annotation: string;
  fragment: {
    alias: string;
    column: string;
    expression: string;
    table: string;
  };
}

/**
 * Where SQL片段
 */
export interface WhereSqlFragment {
  annotation: string;
  fragment: {
    column: string;
    table: string;
    condition: string;
    value?: string;
  };
}

/**
 * SQL 片段
 */
export interface SqlFragments {
  groupBy: GroupBySqlFragment[];
  orderBy: OrderBySqlFragment[];
  select: SelectSqlFragment[];
  where: WhereSqlFragment[];
  tableAlias: string;
}

/**
 * 主表查询SQL片段
 */
export interface PrimarySqlFragment extends SqlFragments {
  from: string;
  name: string;
  analysis: string;
}

/**
 * 从表查询SQL片段
 */
export interface JoinConditions {
  type: string;
  tableName: string;
  tableAliasName: string;
  joinTableName: string;
  joinArray: {
    condition: string;
    joinValue: string;
    tableColumnName: string
  }[]
  // condition: string;
  // tableColumnName: string;
  // joinTableColumnName: string;
  // expression: string;
}

/**
 * 子表查询SQL片段
 */
export interface SubSqlFragments extends SqlFragments {
  join: JoinConditions;
  subSqlId: number;
  subSqlName: string;
  subSql: string;
  analysis: string;
  names: string[];
  parameters: ExternalParamter[];
}

export interface JoinSqlFragments extends SqlFragments {
  join: JoinConditions;
  name: string;
  analysis: string;
}

/**
 * 外部参数
 */
export interface ExternalParamter {
  name: string;
  type: 'table' | 'static' | 'predefined';
  table?: string;
  value: string;
  description: string;
}

/**
 * 数据库表关系
 */
export interface SqlTableRelationship {
  //  从表
  joinTables: {
    name: string;
    alias: string;
    joinName: string;
    analysis: string;
  }[];
  //  主表
  primaryTable: {
    name: string;
    alias: string;
    analysis: string;
  };
  //  子表
  subTables: {
    names: string[];
    analysis: string;
    parameters: ExternalParamter[];
  }[];
}

/**
 * SQL设计器基础数据
 */
export interface SqlDesignerInfo {
  sqlTables: SqlTable[];
  sqlTableRelationship: SqlTableRelationship;
  primaryTableSqlFragment: PrimarySqlFragment;
  subTableSqlFragments: SubSqlFragments[];
  joinTableSqlFragments: JoinSqlFragments[];
  externalParameters: ExternalParamter[];
  sql: string;
  moduleId?: number;
  primarySqlTable: SqlTable;
  [key: string]: any;
}

export interface DataSourceOptions {
  dataSourceId: number;
  dataSourceName: string;
  dbCharset: string;
  dbDescription: string;
  dbName: string;
  dbHost: string;
  dbPort: number;
  dbType: string;
}

export interface DataSourceDetail extends DataSourceOptions {
  dbName: string;
  userName: string;
  password: string;
}
