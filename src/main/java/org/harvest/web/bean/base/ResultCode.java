package org.harvest.web.bean.base;

public enum ResultCode {

	SUCCESS {
		@Override
		public int getCode() {
			return 0;
		}

		@Override
		public String getDesc() {
			return "success";
		}
	},
	ERROR {
		@Override
		public int getCode() {
			return 1;
		}

		@Override
		public String getDesc() {
			return "error";
		}
	},
	VCODE_ERROR {
		@Override
		public int getCode() {
			return 1;
		}

		@Override
		public String getDesc() {
			return "validate code error";
		}
	},
	VCODE_WRONG {
		@Override
		public int getCode() {
			return 1;
		}

		@Override
		public String getDesc() {
			return "validate code is wrong";
		}
	},
	// SYSTEM_CONFIG 100
	SYSTEM_CONFIG_SAVE_ERROR {
		@Override
		public int getCode() {
			return 100001;
		}

		@Override
		public String getDesc() {
			return "add system config error .";
		}
	},
	SYSTEM_CONFIG_DELETE_ERROR {
		@Override
		public int getCode() {
			return 100002;
		}

		@Override
		public String getDesc() {
			return "delete system config error .";
		}
	},
	SYSTEM_CONFIG_SELECT_ERROR {
		@Override
		public int getCode() {
			return 100003;
		}

		@Override
		public String getDesc() {
			return "select system config error .";
		}
	},
	SYSTEM_CONFIG_COPY_ERROR {
		@Override
		public int getCode() {
			return 100004;
		}

		@Override
		public String getDesc() {
			return "copy system config error .";
		}
	},
	USER_NOT_EXISTS {
		@Override
		public int getCode() {
			return 101001;
		}

		@Override
		public String getDesc() {
			return "user does not exists .";
		}
	},
	USER_EXISTS {
		@Override
		public int getCode() {
			return 101001;
		}

		@Override
		public String getDesc() {
			return "user is already exists .";
		}
	},USER_NOLOGIN {
		@Override
		public int getCode() {
			return 101002;
		}

		@Override
		public String getDesc() {
			return "user login is timeout.";
		}
	},PASSWORD_ERROR {
		@Override
		public int getCode() {
			return 101004;
		}

		@Override
		public String getDesc() {
			return "password is wrong.";
		}
	},
	PASSWORD_INCORRECT {
		@Override
		public int getCode() {
			return 101002;
		}

		@Override
		public String getDesc() {
			return "password incorrect .";
		}
	};
	public abstract int getCode();

	public abstract String getDesc();

}
