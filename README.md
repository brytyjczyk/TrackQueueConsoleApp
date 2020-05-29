# TrackQueueConsoleApp
You can run app by executing script run.sh
Requirements java 11
# How it looks
You can put some commands into command line like:\
arrive truck-weight\
  *truck-weight should by an Integer\
  *e.g. arrive 6\
  *which means that truck with weight 6 just arrived\
addTime\
  *it addst one unit to time counting\
print\
  *it prints all truck in queues and show amount of left truck, and arrived that aren't in a queues yet, and show if any truck is processing in one or both places\
truck truck-id\
  *it show state of given truck id\
  *e.g. truck 3\
  *which means that we will get state of truck with id=3\
estimatedTime truck-id\
  *it show estimated time of waiting in a queue right now for a given truck id\
  *e.g. estimatedTime 3\
  *which means that we will get estimated time of waiting of truck with id=3\
exit\
  *which shutdown application\
