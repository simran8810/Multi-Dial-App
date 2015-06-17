# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('calling', '0004_auto_20150508_0657'),
    ]

    operations = [
        migrations.AlterField(
            model_name='usermodel',
            name='feedback',
            field=models.CharField(max_length=100, null=True, blank=True),
            preserve_default=True,
        ),
    ]
