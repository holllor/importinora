"""odbchelper.py sample script

This program is part of "Dive Into Python", a free Python book for
experienced programmers.  Visit http://diveintopython.org/ for the
latest version.

All this stuff at the top of the script is just optional metadata;
the real code starts on the "def buildConnectionString" line
"""

__author__ = "Mark Pilgrim (f8dy@diveintopython.org)"
__version__ = "1.0"
__date__ = "30 May 2001"
__copyright__ = "Copyright (c) 2001 Mark Pilgrim"
__license__ = "Python"

def buildConnectionString(params):
    """Build a connection string from a dictionary

    Returns string.
    """
    return ";".join(["%s=%s" % (k, v) for k, v in params.items()])

if __name__ == "__main__":
    myParams = {"server":"mpilgrim", \
                "database":"master", \
                "uid":"sa", \
                "pwd":"secret"
                }
    print buildConnectionString(myParams)