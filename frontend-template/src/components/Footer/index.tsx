import {GithubOutlined} from '@ant-design/icons';
import {DefaultFooter} from '@ant-design/pro-components';
import React from 'react';

const Footer: React.FC = () => {
  return (
    <DefaultFooter
      style={{
        background: 'none',
      }}
      copyright="Powered by Napir"
      links={[
        {
          key: 'AI辅助系统开发平台',
          title: 'AI辅助系统开发平台',
          href: 'https://www.enapir.com/',
          blankTarget: true,
        },
        {
          key: 'Napir',
          title: 'Focus on your focus',
          href: 'https://www.enapir.com/',
          blankTarget: true,
        },
      ]}
    />
  );
};

export default Footer;
