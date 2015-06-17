# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('calling', '0007_usermodel_campaign'),
    ]

    operations = [
        migrations.CreateModel(
            name='CampaignModel',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('name', models.CharField(max_length=100, null=True, blank=True)),
                ('choice_1', models.CharField(max_length=100, null=True, blank=True)),
                ('choice_2', models.CharField(max_length=100, null=True, blank=True)),
                ('choice_3', models.CharField(max_length=100, null=True, blank=True)),
                ('choice_4', models.CharField(max_length=100, null=True, blank=True)),
            ],
            options={
                'db_table': 'CampaignModel',
            },
            bases=(models.Model,),
        ),
    ]
