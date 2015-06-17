# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('calling', '0005_auto_20150508_0710'),
    ]

    operations = [
        migrations.AddField(
            model_name='usermodel',
            name='callStatus',
            field=models.CharField(max_length=100, null=True, blank=True),
            preserve_default=True,
        ),
    ]
