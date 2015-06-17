from django.shortcuts import render
from django.http import HttpResponse


import json
import random
import xlrd

from django.views.decorators.csrf import csrf_exempt
from models import UserModel, AuthModel, CampaignModel
from CallingSite.settings import CAMPAIGN, CALL_RECORDING

from os.path import dirname as _ ,join
DATA_DIR = join(_(_(__file__)),'contact_files')
# Create your views here.

# Class-Based view used for RPL calculation to be called in process done

@csrf_exempt
def getContact(request):


    requestedCampaign = str(request.POST.get("campaign"))
    print requestedCampaign

    response_data = {}

    try:
        leads  = UserModel.objects.filter(feedback__isnull=True,detailFeedback__isnull=True,
            callStatus="To be called",campaign=requestedCampaign).first()
        print leads.name,leads.number,leads.id

        lead_id = leads.id

        inProcessLead = UserModel.objects.get(id__exact=lead_id)
        inProcessLead.callStatus="In process"
        inProcessLead.save()


        response_data['name'] = inProcessLead.name
        response_data['number'] = inProcessLead.number
        response_data['lead_id'] = inProcessLead.id
        response_data['recorder'] = CALL_RECORDING

        


    except:
        response_data['name']=""
        response_data['number']=""
        response_data['lead_id']=""
        response_data['recorder'] = CALL_RECORDING

    return HttpResponse(json.dumps( response_data ) )


@csrf_exempt
def addDetails(request):

    userLeadId = str(request.POST.get("lead_id"));
    userName = str(request.POST.get("name"))
    userNumber = str(request.POST.get("number"))
    userFeedback = str(request.POST.get("feedback"))
    userDetailFeedback = str(request.POST.get("detailFeedback"))


    calledLead = UserModel.objects.get(id__exact=userLeadId)
    calledLead.feedback = userFeedback
    calledLead.detailFeedback = userDetailFeedback
    calledLead.callStatus = "Called"
    calledLead.save()

    print "inside add details"

    print calledLead,calledLead.feedback, calledLead.detailFeedback, calledLead.callStatus
    #UserModel.objects.create(name=userName,number=userNumber,feedback=userFeedback,detailFeedback=userDetailFeedback)

    
    return HttpResponse("testing")


@csrf_exempt
def authUser(request):

    print "inside"
    print CAMPAIGN

    username = str(request.POST.get("name"))
    password = str(request.POST.get("password"))

    campaignResponseData = {}

    campaignChoices = []


    try:
        user = AuthModel.objects.get(name=username,password=password)

        campaign = CampaignModel.objects.get(name=CAMPAIGN)

        campaignChoices.append(campaign.choice_1)
        campaignChoices.append(campaign.choice_2)
        campaignChoices.append(campaign.choice_3)
        campaignChoices.append(campaign.choice_4)
        
        campaignResponseData['campaign'] = str(campaign)
        campaignResponseData['feedbackChoices'] = campaignChoices
    
        print campaignResponseData


        return HttpResponse(json.dumps( campaignResponseData ))

    except:

        return HttpResponse("failure")



