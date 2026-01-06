import {Alert, Skeleton} from 'antd';
import React from 'react';
import {useIntl, useRequest} from '@umijs/max';
import {queryCurrent} from '../service';
import useStyles from './index.style';
import PhoneEditor from './profile/PhoneEditor';
import EmailEditor from './profile/EmailEditor';

const BaseView: React.FC = () => {
  const {styles} = useStyles();
  const intl = useIntl();
  const {data, loading, refresh} = useRequest(() => queryCurrent());
  const currentUser = data?.data ?? data;

  return (
    <div className={styles.baseView}>
      {loading ? (
        <Skeleton active paragraph={{rows: 4}}/>
      ) : (
        <div className={styles.infoStack}>
          <Alert
            type="info"
            showIcon
            className={styles.helperAlert}
            message={intl.formatMessage({
              id: 'system.personal_information_editing.helper_title',
              defaultMessage: '完善个人资料提升账号安全',
            })}
            description={intl.formatMessage({
              id: 'system.personal_information_editing.helper_description',
              defaultMessage: '绑定邮箱和手机号可用于找回密码与接收关键信息。',
            })}
          />
          <PhoneEditor currentUser={currentUser} onUpdated={refresh}/>
          <EmailEditor currentUser={currentUser} onUpdated={refresh}/>
        </div>
      )}
    </div>
  );
};

export default BaseView;
