from django.conf.urls import patterns, include, url

from django.contrib import admin

from calling.views import getContact, addDetails, authUser
admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'CallingSite.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^admin/', include(admin.site.urls)),
    url(r'^$', getContact, name='get_contact'),
    url(r'^add_details/$', addDetails, name='put_details'),
    url(r'^auth_user/$', authUser, name='auth')
)
