# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('calling', '0006_usermodel_callstatus'),
    ]

    operations = [
        migrations.AddField(
            model_name='usermodel',
            name='campaign',
            field=models.CharField(max_length=100, null=True, blank=True),
            preserve_default=True,
        ),
    ]
