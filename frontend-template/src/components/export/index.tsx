import get from '@/utils/get';
import { DownloadOutlined } from '@ant-design/icons';
import { Button, Dropdown, Menu, message } from 'antd';
import dayjs from 'dayjs';
import React, { useState } from 'react';
import * as XLSX from 'xlsx';

export type ExportType = 'currentPage' | 'specified' | 'all';

interface ColumnConfig {
  header: string; // Display name for the column
  key: string; // Key in the data object
}

interface ExportButtonProps {
  fetchData: (type: ExportType, pageSize?: number) => Promise<any[]>;
  fileName?: string; // Custom file name
  columnsConfig: ColumnConfig[]; // Combined headers and columns
}

const toArray = (key: string): string[] => {
  return key.split('.');
};

const ExportButton: React.FC<ExportButtonProps> = ({
  fetchData,
  fileName = 'exported_data',
  columnsConfig,
}) => {
  const [loading, setLoading] = useState(false);

  const handleExport = async (type: ExportType) => {
    setLoading(true);
    try {
      let data;
      switch (type) {
        case 'specified': {
          const limit = prompt('请输入要导出的条数:');
          if (!limit || isNaN(Number(limit))) {
            message.error('请输入有效的数字');
            return;
          }
          data = await fetchData(type, Number(limit));
          break;
        }
        case 'all':
        case 'currentPage':
          data = await fetchData(type);
          break;
        default:
          data = [];
      }

      if (!data.length) {
        message.warning('没有数据可导出');
        return;
      }

      // Map data to include only the specified columns
      const mappedData = data.map((item) => {
        const row: Record<string, any> = {};
        columnsConfig.forEach(({ key }) => {
          let value = get(item, toArray(key));
          if (Array.isArray(value) || Object.prototype.toString.call(value) === '[object Object]') {
            row[key] = JSON.stringify(value);
          } else {
            row[key] = value;
          }
        });
        return row;
      });

      // Extract headers from columnsConfig
      const headers = columnsConfig.map(({ header }) => header);
      // Create worksheet with headers and data
      const ws = XLSX.utils.json_to_sheet(mappedData);
      XLSX.utils.sheet_add_aoa(ws, [headers], { origin: 'A1' });
      const wb = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');
      const file = `${fileName || 'Export Data'}-${dayjs().format('YYYY_MM_DD_HH_mm')}.xlsx`;
      XLSX.writeFile(wb, file);
      message.success('导出成功');
    } catch (error) {
      message.error('导出失败');
    } finally {
      setLoading(false);
    }
  };

  const menu = (
    <Menu>
      <Menu.Item key="currentPage" onClick={() => handleExport('currentPage')}>
        导出当前页
      </Menu.Item>
      <Menu.Item key="specified" onClick={() => handleExport('specified')}>
        导出指定条数
      </Menu.Item>
      <Menu.Item key="all" onClick={() => handleExport('all')}>
        导出全部数据
      </Menu.Item>
    </Menu>
  );

  return (
    <Dropdown overlay={menu} trigger={['click']}>
      <Button type="primary" icon={<DownloadOutlined />} loading={loading}>
        导出
      </Button>
    </Dropdown>
  );
};

export default ExportButton;
