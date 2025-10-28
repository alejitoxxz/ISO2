package co.edu.uco.ucochallenge.user.findusers.application.usecase.domain;

import co.edu.uco.ucochallenge.crosscuting.helper.NumberHelper;

public class FindUsersByFilterInputDomain {

        private static final int DEFAULT_PAGE = 0;
        private static final int DEFAULT_SIZE = 10;

        private final int page;
        private final int size;

        private FindUsersByFilterInputDomain(final Builder builder) {
                this.page = sanitizePage(builder.page);
                this.size = sanitizeSize(builder.size);
        }

        public static Builder builder() {
                return new Builder();
        }

        public int getPage() {
                return page;
        }

        public int getSize() {
                return size;
        }

        private int sanitizePage(final Integer page) {
                final var defaultValue = DEFAULT_PAGE;
                final var sanitizedPage = NumberHelper.getDefault(page, defaultValue);
                return Math.max(0, sanitizedPage);
        }

        private int sanitizeSize(final Integer size) {
                final var defaultValue = DEFAULT_SIZE;
                final var sanitizedSize = NumberHelper.getDefault(size, defaultValue);
                return NumberHelper.ensureRange(sanitizedSize, 1, 100, defaultValue);
        }

        public static final class Builder {

                private Integer page = DEFAULT_PAGE;
                private Integer size = DEFAULT_SIZE;

                public Builder page(final Integer page) {
                        this.page = page;
                        return this;
                }

                public Builder size(final Integer size) {
                        this.size = size;
                        return this;
                }

                public FindUsersByFilterInputDomain build() {
                        return new FindUsersByFilterInputDomain(this);
                }
        }
}
