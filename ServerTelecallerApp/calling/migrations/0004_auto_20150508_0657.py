# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('calling', '0003_authmodel'),
    ]

    operations = [
        migrations.AlterField(
            model_name='usermodel',
            name='feedback',
            field=models.CharField(max_length=1, null=True, blank=True),
            preserve_default=True,
        ),
    ]
