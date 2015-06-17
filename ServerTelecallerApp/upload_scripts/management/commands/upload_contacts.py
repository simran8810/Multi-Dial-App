from django.core.management.base import BaseCommand
import xlrd
from calling.models import UserModel, CampaignModel
from os.path import dirname as _ ,join
DATA_DIR = join(_(_(_(_(__file__)))),'contact_files')


class Command(BaseCommand):
    def handle(self,*args, **options):
	
        self.populate_contacts()
        #self.populate_campaigns()

    def populate_contacts(self):
        file_name = join(DATA_DIR,'contacts.xlsx')
        book=xlrd.open_workbook(file_name)
        sheet=book.sheet_by_name('sheet1')

        start_index = 1
        end_index = sheet.nrows

        while start_index < end_index:

            name = sheet.cell_value(rowx=start_index, colx=0)
            number = sheet.cell_value(rowx=start_index, colx=1)
            campaign = sheet.cell_value(rowx=start_index, colx=2)

            print name,int(number)

            UserModel.objects.create(name=name,number=int(number),callStatus="To be called",campaign=campaign)

            start_index += 1

    def populate_campaigns(self):
        file_name = join(DATA_DIR,'radio_button_choices.xlsx')
        book = xlrd.open_workbook(file_name)
        sheet = book.sheet_by_name('sheet')

        start_col_index = 0
        end_col_index = sheet.ncols

        while start_col_index < end_col_index:

            campaign = sheet.cell_value(rowx=0, colx=start_col_index)

            choice1 = sheet.cell_value(rowx=1, colx=start_col_index)
            choice2 = sheet.cell_value(rowx=2, colx=start_col_index)
            choice3 = sheet.cell_value(rowx=3, colx=start_col_index)
            choice4 = sheet.cell_value(rowx=4, colx=start_col_index)

            CampaignModel.objects.create(name=campaign,choice_1=choice1,choice_2=choice2,choice_3=choice3,choice_4=choice4)

            start_col_index += 1   









