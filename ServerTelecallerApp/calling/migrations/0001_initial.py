# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='UserModel',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('name', models.CharField(max_length=100, null=True, blank=True)),
                ('number', models.CharField(max_length=100, null=True, blank=True)),
                ('feedback', models.CharField(blank=True, max_length=1, null=True, choices=[(b'0', b'No Answer'), (b'1', b'Call Later'), (b'2', b'Received'), (b'3', b'Feedback')])),
            ],
            options={
                'db_table': 'UserModel',
            },
            bases=(models.Model,),
        ),
    ]
