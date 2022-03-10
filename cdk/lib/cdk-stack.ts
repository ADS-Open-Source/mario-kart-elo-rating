import * as cdk from '@aws-cdk/core';
import * as iam from '@aws-cdk/aws-iam'
import * as elasticbeanstalk from '@aws-cdk/aws-elasticbeanstalk'

export class CdkStack extends cdk.Stack {
  constructor(scope: cdk.Construct, id: string, props?: cdk.StackProps) {
    super(scope, id, props);

    // EBS IAM Roles
    const EbInstanceRole = new iam.Role(this, 'mkelo-aws-elasticbeanstalk-ec2-role', {
      assumedBy: new iam.ServicePrincipal('ec2.amazonaws.com'),
    });

    const managedPolicy = iam.ManagedPolicy.fromAwsManagedPolicyName('AWSElasticBeanstalkWebTier')
    EbInstanceRole.addManagedPolicy(managedPolicy);

    const instanceProfile = new iam.CfnInstanceProfile(this, 'mkelo-InstanceProfile', {
      instanceProfileName: 'mkelo-InstanceProfile',
      roles: [
        EbInstanceRole.roleName
      ]
    });


    // EBS Application and Environment
    const app = new elasticbeanstalk.CfnApplication(this, 'Application', {
      applicationName: 'mkelo-EB-App'
    });

    const node = this.node;
    const platform = node.tryGetContext("platform");

    const optionSettingProperties: elasticbeanstalk.CfnEnvironment.OptionSettingProperty[] = [
      {
        namespace: 'aws:autoscaling:launchconfiguration',
        optionName: 'InstanceType',
        value: 't3.micro',
      },
      {
        namespace: 'aws:autoscaling:launchconfiguration',
        optionName: 'IamInstanceProfile',
        value: instanceProfile.instanceProfileName
      },
      {
        namespace: 'aws:elasticbeanstalk:environment',
        optionName: 'EnvironmentType',
        value: 'SingleInstance'
      },
      {
        namespace: 'aws:elasticbeanstalk:application:environment',
        optionName: 'PORT',
        value: '8080'
      },
      {
        namespace: 'aws:elasticbeanstalk:environment:process:default',
        optionName: 'HealthCheckPath ',
        value: '/api/player'
      },
      {
        namespace: 'aws:elasticbeanstalk:environment:process:default',
        optionName: 'Port',
        value: '8080'
      }
    ];

    const env = new elasticbeanstalk.CfnEnvironment(this, 'Environment', {
      environmentName: 'mkelo-EB-Env',
      applicationName: 'mkelo-EB-App',
      platformArn: platform,
      solutionStackName: '64bit Amazon Linux 2 v3.2.12 running Corretto 11',
      optionSettings: optionSettingProperties
    });

    env.addDependsOn(app);

  }
}
