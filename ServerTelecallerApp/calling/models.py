from django.db import models

# Create your models here.
class UserModel(models.Model):

    """ User model for calling details of candidate """
    # noAnswer = '0'
    # callLater = '1'
    # received = '2'
    # feedback = '3'

    # FEEDBACK_CHOICES = (
    #     (noAnswer, 'No Answer'),
    #     (callLater, 'Call Later'),
    #     (received, 'Received'),
    #     (feedback, 'Feedback')
    # )
    

    name = models.CharField(max_length=100,null=True, blank=True)

    number = models.CharField(max_length=100,null=True, blank=True)

    feedback = models.CharField( max_length=100, null=True, blank=True) 

    detailFeedback = models.CharField(max_length=100,null=True, blank=True)

    callStatus  = models.CharField(max_length=100,null=True, blank=True)

    campaign = models.CharField(max_length=100,null=True, blank=True)


    class Meta:
        db_table = 'UserModel'

    def __unicode__(self):
        return self.name

# Create your models here.
class AuthModel(models.Model):

    """ Auth model to authenticate telecaller """


    name = models.CharField(max_length=100,null=True, blank=True)

    password = models.CharField(max_length=100,null=True, blank=True)


    class Meta:
        db_table = 'AuthModel'

    def __unicode__(self):
        return self.name


# Create your models here.
class CampaignModel(models.Model):

    """ Auth model to authenticate telecaller """


    name = models.CharField(max_length=100,null=True, blank=True)

    choice_1 = models.CharField(max_length=100,null=True, blank=True)

    choice_2 = models.CharField(max_length=100,null=True, blank=True)

    choice_3 = models.CharField(max_length=100,null=True, blank=True)

    choice_4 = models.CharField(max_length=100,null=True, blank=True)


    class Meta:
        db_table = 'CampaignModel'

    def __unicode__(self):
        return self.name

