import React, {useEffect, useRef, useState} from 'react';
import {Button, Card, Form, Input, message, Result, Space} from 'antd';
import {LockOutlined, MailOutlined, SafetyOutlined} from '@ant-design/icons';
import {FormattedMessage, Helmet, useIntl} from '@umijs/max';
import {resetPassword, sendEmailVerifyCode} from '@/common/service/login';
import {sha256Hex} from '@/utils/crypto';

const ResetPassword: React.FC = () => {
  const [form] = Form.useForm();
  const intl = useIntl();
  const [countdown, setCountdown] = useState(0);
  const timerRef = useRef<NodeJS.Timeout | null>(null);
  const [submitting, setSubmitting] = useState(false);
  const [success, setSuccess] = useState(false);

  useEffect(() => () => {
    if (timerRef.current) {
      clearInterval(timerRef.current);
    }
  }, []);

  const requestVerifyCode = async () => {
    try {
      const {email} = await form.validateFields(['email']);
      await sendEmailVerifyCode({email, bizType: 'password_reset'});
      message.success(
        intl.formatMessage({
          id: 'pages.resetPassword.codeSent',
          defaultMessage: '验证码已发送，请检查邮箱',
        }),
      );
      setCountdown(180);
      timerRef.current = setInterval(() => {
        setCountdown((prev) => {
          if (prev <= 1) {
            if (timerRef.current) {
              clearInterval(timerRef.current);
            }
            return 0;
          }
          return prev - 1;
        });
      }, 1000);
    } catch (error: any) {
      if (error?.errorFields) {
        return;
      }
      message.error(error?.message || intl.formatMessage({
        id: 'pages.resetPassword.codeFailed',
        defaultMessage: '发送验证码失败，请稍后重试',
      }));
    }
  };

  const onFinish = async (values: any) => {
    if (values.newPassword !== values.confirmPassword) {
      message.error(
        intl.formatMessage({
          id: 'pages.resetPassword.passwordMismatch',
          defaultMessage: '两次输入的密码不一致',
        }),
      );
      return;
    }
    try {
      setSubmitting(true);
      const hashed = await sha256Hex(values.newPassword);
      const payload = {
        email: values.email,
        verifyCode: values.verifyCode,
        newPassword: hashed,
        confirmPassword: hashed,
      };
      const response = await resetPassword(payload);
      if (response?.code === 200) {
        message.success(
          intl.formatMessage({
            id: 'pages.resetPassword.successToast',
            defaultMessage: '密码重置成功',
          }),
        );
        setSuccess(true);
      } else {
        message.error(response?.message || intl.formatMessage({
          id: 'pages.resetPassword.failure',
          defaultMessage: '密码重置失败，请稍后重试',
        }));
      }
    } catch (error: any) {
      message.error(error?.message || intl.formatMessage({
        id: 'pages.resetPassword.failure',
        defaultMessage: '密码重置失败，请稍后重试',
      }));
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div
      style={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        position: 'relative',
        backgroundImage: "url('/loginbg.jpg')",
        backgroundSize: 'cover',
        backgroundPosition: 'center',
      }}
    >
      <Helmet>
        <title>
          {intl.formatMessage({
            id: 'pages.login.forgotPassword',
            defaultMessage: '忘记密码',
          })}
        </title>
      </Helmet>
      <div
        style={{
          position: 'absolute',
          inset: 0,
          background: 'rgba(0,0,0,0.35)',
        }}
      />
      <Card
        bordered={false}
        style={{
          borderRadius: 16,
          width: '90%',
          maxWidth: 420,
          position: 'relative',
        }}
      >
        {!success ? (
          <Form layout="vertical" form={form} onFinish={onFinish} autoComplete="off">
            <Form.Item
              name="email"
              label={
                <FormattedMessage id="pages.resetPassword.email" defaultMessage="邮箱"/>
              }
              rules={[
                {
                  required: true,
                  message: (
                    <FormattedMessage
                      id="pages.resetPassword.emailRequired"
                      defaultMessage="请输入邮箱"
                    />
                  ),
                },
                {
                  type: 'email',
                  message: (
                    <FormattedMessage
                      id="pages.resetPassword.emailInvalid"
                      defaultMessage="邮箱格式不正确"
                    />
                  ),
                },
              ]}
            >
              <Input prefix={<MailOutlined/>} placeholder="email@example.com"/>
            </Form.Item>
            <Form.Item
              label={<FormattedMessage id="pages.resetPassword.code" defaultMessage="验证码"/>}
              required
            >
              <Space.Compact style={{width: '100%'}}>
                <Form.Item
                  name="verifyCode"
                  noStyle
                  rules={[
                    {
                      required: true,
                      message: (
                        <FormattedMessage
                          id="pages.resetPassword.codeRequired"
                          defaultMessage="请输入验证码"
                        />
                      ),
                    },
                  ]}
                >
                  <Input prefix={<SafetyOutlined/>} placeholder="123456"/>
                </Form.Item>
                <Button onClick={requestVerifyCode} disabled={countdown > 0}>
                  {countdown > 0
                    ? `${countdown}s`
                    : intl.formatMessage({
                      id: 'pages.resetPassword.getCode',
                      defaultMessage: '获取验证码',
                    })}
                </Button>
              </Space.Compact>
            </Form.Item>
            <Form.Item
              name="newPassword"
              label={<FormattedMessage id="pages.resetPassword.newPassword" defaultMessage="新密码"/>}
              rules={[
                {
                  required: true,
                  message: (
                    <FormattedMessage
                      id="pages.resetPassword.newPasswordRequired"
                      defaultMessage="请输入新密码"
                    />
                  ),
                },
              ]}
            >
              <Input.Password prefix={<LockOutlined/>} placeholder="******"/>
            </Form.Item>
            <Form.Item
              name="confirmPassword"
              label={<FormattedMessage id="pages.resetPassword.confirmPassword" defaultMessage="确认密码"/>}
              rules={[
                {
                  required: true,
                  message: (
                    <FormattedMessage
                      id="pages.resetPassword.confirmPasswordRequired"
                      defaultMessage="请再次输入密码"
                    />
                  ),
                },
              ]}
            >
              <Input.Password prefix={<LockOutlined/>} placeholder="******"/>
            </Form.Item>
            <Button type="primary" block htmlType="submit" loading={submitting}>
              <FormattedMessage id="pages.resetPassword.submit" defaultMessage="重置密码"/>
            </Button>
          </Form>
        ) : (
          <Result
            status="success"
            title={
              <FormattedMessage
                id="pages.resetPassword.success"
                defaultMessage="密码已重置"
              />
            }
            extra={[
              <Button type="primary" key="login" href="/user/login">
                <FormattedMessage id="pages.resetPassword.goLogin" defaultMessage="返回登录"/>
              </Button>,
            ]}
          />
        )}
      </Card>
    </div>
  );
};

export default ResetPassword;
