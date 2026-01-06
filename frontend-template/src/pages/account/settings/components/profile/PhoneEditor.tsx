import React, {useEffect, useMemo, useState} from 'react';
import {Button, Form, Input, Modal, Space, Tag, Typography, message} from 'antd';
import {WarningFilled} from '@ant-design/icons';
import {useIntl} from '@umijs/max';
import useStyles from '../index.style';
import type {CurrentUser} from '../../data';
import {updateOperatorInfo} from '@/common/api/common/login';

const {Paragraph, Text} = Typography;

type PhoneEditorProps = {
  currentUser?: CurrentUser;
  onUpdated?: () => void;
};

const PhoneEditor: React.FC<PhoneEditorProps> = ({currentUser, onUpdated}) => {
  const {styles} = useStyles();
  const intl = useIntl();
  const [modalVisible, setModalVisible] = useState(false);
  const [form] = Form.useForm();
  const [saving, setSaving] = useState(false);
  const watchedMobile = Form.useWatch('mobile', form);

  const mobile = currentUser?.mobile || currentUser?.associatedMobileNumber;
  const phoneLinked = Boolean(mobile);

  useEffect(() => {
    if (modalVisible) {
      form.setFieldsValue({mobile});
    } else {
      form.resetFields();
    }
  }, [modalVisible, mobile, form]);

  const hasChanged = useMemo(() => {
    return watchedMobile !== undefined && watchedMobile !== mobile;
  }, [watchedMobile, mobile]);

  const handleSave = async () => {
    try {
      const values = await form.validateFields();
      if (!values.mobile || values.mobile === mobile) {
        return;
      }
      setSaving(true);
      await updateOperatorInfo({mobile: values.mobile});
      message.success(intl.formatMessage({id: 'common.update_success', defaultMessage: '更新成功'}));
      setModalVisible(false);
      onUpdated?.();
    } catch (error: any) {
      if (!error?.errorFields) {
        message.error(error?.message || intl.formatMessage({
          id: 'common.request_failed',
          defaultMessage: '请求失败',
        }));
      }
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className={styles.infoCard}>
      <div className={styles.cardHeader}>
        <Space align="start">
          <WarningFilled className={styles.warningIcon}/>
          <div>
            <Text className={styles.cardTitle}>
              {intl.formatMessage({
                id: 'system.personal_information_editing.mobile',
                defaultMessage: '绑定手机号',
              })}
            </Text>
            <Paragraph className={styles.cardDescription}>
              {intl.formatMessage({
                id: 'system.personal_information_editing.phone_description',
                defaultMessage: '用于账户安全验证和通知',
              })}
            </Paragraph>
          </div>
        </Space>
        <Button type="primary" ghost onClick={() => setModalVisible(true)}>
          {intl.formatMessage({id: 'system.personal_information_editing.modify_button', defaultMessage: '修改'})}
        </Button>
      </div>
      <div className={styles.cardStatusRow}>
        <Tag color={phoneLinked ? 'green' : 'warning'}>
          {phoneLinked
            ? intl.formatMessage({id: 'system.personal_information_editing.phone_bound', defaultMessage: '已绑定'})
            : intl.formatMessage({id: 'system.personal_information_editing.phone_unbound', defaultMessage: '未绑定'})}
        </Tag>
        <Text className={styles.cardValue}>{phoneLinked ? mobile : '--'}</Text>
      </div>

      <Modal
        open={modalVisible}
        onCancel={() => setModalVisible(false)}
        onOk={handleSave}
        okButtonProps={{disabled: !hasChanged}}
        confirmLoading={saving}
        title={intl.formatMessage({id: 'system.personal_information_editing.phone_modal_title', defaultMessage: '修改手机号'})}
      >
        <Paragraph type="secondary">
          {intl.formatMessage({
            id: 'system.personal_information_editing.phone_modal_tips',
            defaultMessage: '更新手机号后将用于登录及安全校验。',
          })}
        </Paragraph>
        <Form layout="vertical" form={form}>
          <Form.Item
            name="mobile"
            label={intl.formatMessage({id: 'account.mobile', defaultMessage: '手机号'})}
            rules={[
              {required: true, message: intl.formatMessage({id: 'common.validator.required', defaultMessage: '请输入必填项'})},
            ]}
          >
            <Input placeholder={intl.formatMessage({id: 'system.personal_information_editing.phone_placeholder', defaultMessage: '请输入新的手机号'})}/>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default PhoneEditor;
