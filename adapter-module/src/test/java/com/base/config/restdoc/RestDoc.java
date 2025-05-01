package com.base.config.restdoc;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.resourceDetails;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;

import com.epages.restdocs.apispec.ResourceSnippetDetails;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.headers.RequestHeadersSnippet;
import org.springframework.restdocs.headers.ResponseHeadersSnippet;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.RequestBodySnippet;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseBodySnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.request.QueryParametersSnippet;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.request.RequestPartDescriptor;
import org.springframework.restdocs.request.RequestPartsSnippet;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.util.StringUtils;


public class RestDoc {

    private final String identifier;
    private final RequestFieldsSnippet requestFieldsSnippet;
    private final PathParametersSnippet pathParametersSnippet;
    private final RequestBodySnippet requestBodySnippet;
    private final QueryParametersSnippet queryParametersSnippet;
    private final RequestHeadersSnippet requestHeadersSnippet;
    private final ResponseFieldsSnippet responseFieldsSnippet;
    private final ResponseHeadersSnippet responseHeadersSnippet;
    private final RequestPartsSnippet requestPartsSnippet;
    private final ResourceSnippetDetails tag;


    private RestDoc(
        String identifier,
        RequestFieldsSnippet requestFieldsSnippet,
        PathParametersSnippet pathParametersSnippet,
        RequestBodySnippet requestBodySnippet,
        QueryParametersSnippet queryParametersSnippet,
        RequestHeadersSnippet requestHeadersSnippet,
        ResponseFieldsSnippet responseFieldsSnippet,
        ResponseHeadersSnippet responseHeadersSnippet,
        RequestPartsSnippet requestPartsSnippet,
        ResourceSnippetDetails tag
    ) {
        this.identifier = identifier;
        this.pathParametersSnippet = pathParametersSnippet;
        this.requestFieldsSnippet = requestFieldsSnippet;
        this.queryParametersSnippet = queryParametersSnippet;
        this.requestBodySnippet = requestBodySnippet;
        this.requestHeadersSnippet = requestHeadersSnippet;
        this.responseFieldsSnippet = responseFieldsSnippet;
        this.responseHeadersSnippet = responseHeadersSnippet;
        this.requestPartsSnippet = requestPartsSnippet;
        this.tag = tag;
    }

    public static RestDocBuilder builder() {
        return new RestDocBuilder();
    }

    private void assertion() {
        assert StringUtils.hasText(identifier) : "identifier is null";
        assert Objects.nonNull(this.requestFieldsSnippet) ||
            Objects.nonNull(this.queryParametersSnippet) ||
            Objects.nonNull(this.requestBodySnippet) ||
            Objects.nonNull(this.requestHeadersSnippet) ||
            Objects.nonNull(this.responseFieldsSnippet) ||
            Objects.nonNull(this.responseHeadersSnippet) : "need to fill document(header or body or query)";


    }

    private RestDocumentationResultHandler documentation() {
        this.assertion();
        Set<Snippet> snippets = new HashSet<>();

        if (Objects.nonNull(this.pathParametersSnippet)) {
            snippets.add(this.pathParametersSnippet);
        }
        if (Objects.nonNull(this.requestHeadersSnippet)) {
            snippets.add(this.requestHeadersSnippet);
        }
        if (Objects.nonNull(this.requestBodySnippet)) {
            snippets.add(this.requestBodySnippet);
        }
        if (Objects.nonNull(this.requestFieldsSnippet)) {
            snippets.add(this.requestFieldsSnippet);
        }
        if (Objects.nonNull(this.responseFieldsSnippet)) {
            snippets.add(this.responseFieldsSnippet);
        }
        if (Objects.nonNull(this.responseHeadersSnippet)) {
            snippets.add(this.responseHeadersSnippet);
        }
        if (Objects.nonNull(this.queryParametersSnippet)) {
            snippets.add(this.queryParametersSnippet);
        }
        if (Objects.nonNull(this.requestPartsSnippet)) {
            snippets.add(this.requestPartsSnippet);
        }
        if (Objects.isNull(this.tag.getTags())) {
            this.tag.tag("api");
        }

        return document(
            this.identifier,
            this.tag,
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            snippets.toArray(Snippet[]::new)
        );
    }


    public static class RestDocBuilder {

        private String identifier;
        private RequestFieldsSnippet requestFieldsSnippet;
        private PathParametersSnippet pathParametersSnippet;
        private RequestBodySnippet requestBodySnippet;
        private QueryParametersSnippet queryParametersSnippet;
        private RequestHeadersSnippet requestHeadersSnippet;
        private ResponseFieldsSnippet responseFieldsSnippet;
        private ResponseHeadersSnippet responseHeadersSnippet;
        private ResponseBodySnippet responseBodySnippet;
        private RequestPartsSnippet requestPartsSnippet;
        private ResourceSnippetDetails tag = resourceDetails();


        private RestDocBuilder() {
        }


        public RestDocBuilder identifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        public RestDocBuilder tag(String... tags) {
            this.tag = this.tag.tags(tags);
            return this;
        }

        public RestDocBuilder summary(String summary) {
            this.tag = this.tag.summary(summary);
            return this;
        }

        public RestDocBuilder description(String description) {
            this.tag = this.tag.description(description);
            return this;
        }


        private Function<Header, HeaderDescriptor> mapHeader = header -> {
            HeaderDescriptor descriptor = headerWithName(header.name());
            if (header.optional()) {
                descriptor.optional();
            }
            if (StringUtils.hasText(header.description())) {
                descriptor.description(header.description());
            }

            return descriptor;
        };
        private Function<Body, FieldDescriptor> mapField = field -> {
            FieldDescriptor descriptor = fieldWithPath(field.name());
            descriptor.type(field.jsonType());
            if (field.optional()) {
                descriptor.optional();
            }
            if (StringUtils.hasText(field.description())) {
                descriptor.description(field.description());
            }

            return descriptor;
        };
        private Function<Query, ParameterDescriptor> mapQuery = field -> {
            ParameterDescriptor descriptor = parameterWithName(field.name());
            if (field.optional()) {
                descriptor.optional();
            }
            if (StringUtils.hasText(field.description())) {
                descriptor.description(field.description());
            }

            return descriptor;
        };


        private Function<PathParameter, ParameterDescriptor> mapPath = field -> {
            ParameterDescriptor descriptor = parameterWithName(field.name());
            if (field.optional()) {
                descriptor.optional();
            }
            if (StringUtils.hasText(field.description())) {
                descriptor.description(field.description());
            }

            return descriptor;
        };

        private Function<Part, RequestPartDescriptor> mapPart = part -> {
            RequestPartDescriptor descriptor = partWithName(part.name());
            if (part.optional()) {
                descriptor.optional();
            }
            if (StringUtils.hasText(part.description())) {
                descriptor.description(part.description());
            }
            return descriptor;
        };


        private HeaderDescriptor[] toHeaderDescriptors(Header... headers) {
            return Arrays.stream(headers).map(mapHeader).toArray(HeaderDescriptor[]::new);
        }

        private FieldDescriptor[] toFieldDescriptors(Body... fields) {
            return Arrays.stream(fields).map(mapField).toArray(FieldDescriptor[]::new);
        }

        private ParameterDescriptor[] toPathDescriptors(PathParameter... paths) {
            return Arrays.stream(paths).map(mapPath).toArray(ParameterDescriptor[]::new);
        }

        private ParameterDescriptor[] toQueryDescriptors(Query... queries) {
            return Arrays.stream(queries).map(mapQuery).toArray(ParameterDescriptor[]::new);
        }

        private RequestPartDescriptor[] toPartDescriptor(Part... parts) {
            return Arrays.stream(parts).map(mapPart).toArray(RequestPartDescriptor[]::new);
        }


        public RestDocBuilder withRequestHeader(Header... headers) {
            requestHeadersSnippet = requestHeaders(this.toHeaderDescriptors(headers));
            return this;
        }

        public RestDocBuilder withRequestFields(Body... bodies) {
            this.requestFieldsSnippet = requestFields(toFieldDescriptors(bodies));
            return this;
        }


        public RestDocBuilder withPathParameters(PathParameter... pathParameters) {
            this.pathParametersSnippet = RequestDocumentation.pathParameters(toPathDescriptors(pathParameters));
            return this;
        }

        public RestDocBuilder withQueryParameters(Query... queries) {
            this.queryParametersSnippet = queryParameters(this.toQueryDescriptors(queries));
            return this;
        }

        public RestDocBuilder withResponseHeaders(Header... headers) {
            this.responseHeadersSnippet = responseHeaders(this.toHeaderDescriptors(headers));
            return this;
        }

        public RestDocBuilder withResponseFields(Body... body) {
            this.responseFieldsSnippet = responseFields(this.toFieldDescriptors(body));
            return this;
        }

        public RestDocBuilder withResponseBody() {
            this.responseBodySnippet = responseBody();
            return this;
        }

        public RestDocBuilder withRequestPart(Part... part) {
            this.requestPartsSnippet = requestParts(this.toPartDescriptor(part));
            return this;
        }


        public RestDocumentationResultHandler build() {

            return new RestDoc(
                this.identifier,
                this.requestFieldsSnippet,
                this.pathParametersSnippet,
                this.requestBodySnippet,
                this.queryParametersSnippet,
                this.requestHeadersSnippet,
                this.responseFieldsSnippet,
                this.responseHeadersSnippet,
                this.requestPartsSnippet,
                this.tag
            ).documentation();
        }

    }

}
